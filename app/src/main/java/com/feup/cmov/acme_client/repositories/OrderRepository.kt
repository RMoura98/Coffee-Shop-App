package com.feup.cmov.acme_client.repositories

import androidx.lifecycle.LiveData
import com.feup.cmov.acme_client.utils.PreferencesUtils
import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.OrderItem
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.network.Result
import com.feup.cmov.acme_client.network.WebService
import com.feup.cmov.acme_client.network.requests.PlaceOrderRequest
import com.feup.cmov.acme_client.network.responses.FetchOrdersResponse
import com.feup.cmov.acme_client.network.responses.PlaceOrderResponse
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import com.feup.cmov.acme_client.utils.ShowFeedback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
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
    ): Order {
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
            appDatabaseDao.markVouchersAsUsed(vouchers.map { voucher ->
                Voucher(
                    voucherId = voucher.voucherId,
                    userId = voucher.userId,
                    voucherType = voucher.voucherType,
                    used_on_order_id = orderId
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
            order
        }
    }
}