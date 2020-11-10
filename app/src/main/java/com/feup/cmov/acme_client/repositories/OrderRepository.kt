package com.feup.cmov.acme_client.repositories

import androidx.lifecycle.LiveData
import com.feup.cmov.acme_client.utils.PreferencesUtils
import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.OrderItem
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.database.models.composed_models.ItemsWithInfo
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.network.WebService
import com.feup.cmov.acme_client.network.responses.FetchOrdersResponse
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import com.feup.cmov.acme_client.utils.ShowFeedback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class OrderRepository
@Inject constructor(
    private val webService: WebService,
    private val appDatabaseDao: AppDatabaseDao
) {

    fun getOrders(): LiveData<List<OrderWithItems>> {
        val (_, uuid) = PreferencesUtils.getLoggedInUser()

        val cached = appDatabaseDao.getOrdersWithItems(uuid!!)
        GlobalScope.launch {
            refreshOrders()
        }

        return cached
    }

    private suspend fun refreshOrders() {
        withContext(Dispatchers.IO) {
            try {
                val result: FetchOrdersResponse = webService.fetchOrders()
                val orders = result.orders
                val orderItems = result.orderItems
                appDatabaseDao.createOrders(orders)
                appDatabaseDao.createOrderItems(orderItems)
            } catch (e: Exception) {
                ShowFeedback.makeSnackbar(e.toString())
                throw e;
            }
        }
    }

    // Place order locally.
    suspend fun placeOrder(
        cartItems: Collection<CartViewModel.CartItem>,
        vouchers: Collection<Voucher>,
        total: Float
    ): OrderWithItems {
        return withContext(Dispatchers.IO) {
            val orderId = UUID.randomUUID().toString()
            val order = Order(
                order_id = orderId,
                order_sequential_id = 0,
                createdAt = Date(),
                updatedAt = Date(),
                total = total,
                completed = false,
                userId = PreferencesUtils.getLoggedInUser().second!!
            )
            appDatabaseDao.createOrder(order)
            // Mark vouchers as used.
            appDatabaseDao.updateVouchers(vouchers.map { voucher ->
                Voucher(
                    voucherId = voucher.voucherId,
                    userId = voucher.userId,
                    voucherType = voucher.voucherType,
                    used_on_order_id = orderId,
                    received_from_order_id = voucher.received_from_order_id
                )
            })
            val orderItems = ArrayList<OrderItem>()
            for (item in cartItems) {
                orderItems.add(
                    OrderItem(
                        order_item_id = orderId,
                        order_id = order.order_id,
                        quantity = item.quantity.toLong(),
                        price = item.item.price,
                        item_id = item.item.id
                    )
                )
            }
            appDatabaseDao.createOrderItems(orderItems)
            val orderItemsWithInfo = ArrayList<ItemsWithInfo>()
            for(item in orderItems) {
                val menuItem = cartItems.filter { it.item.id == item.item_id }[0].item
                orderItemsWithInfo.add(
                    ItemsWithInfo(orderItem = item, menuItem = menuItem)
                )
            }
            OrderWithItems(order=order, orderItems = orderItemsWithInfo, vouchers = ArrayList(vouchers))
        }
    }

    suspend fun removeOrder(order: OrderWithItems): Void {
        return withContext(Dispatchers.IO) {
            appDatabaseDao.removeOrder(order.order)
            appDatabaseDao.removeOrderItems(order.orderItems.map { it.orderItem })
            appDatabaseDao.updateVouchers(order.vouchers.map {voucher ->
                Voucher(voucherId = voucher.voucherId, userId = voucher.userId, voucherType = voucher.voucherType, used_on_order_id = null, received_from_order_id = voucher.received_from_order_id)
            })
        }
    }

    suspend fun hasOrderBeenPickedUp(order: Order): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val orderStatus = webService.getOrderStatus(order.order_id)
                appDatabaseDao.createVouchers(orderStatus.vouchers_received)
                var updatedOrder = Order(order_id = order.order_id, userId = order.userId, order_sequential_id = orderStatus.order_sequential_id, createdAt = order.createdAt, updatedAt = order.updatedAt, completed = true, total = order.total)
                appDatabaseDao.updateOrder(updatedOrder)
                true
            }
            catch (e: HttpException) {
                false
            }
        }
    }
}