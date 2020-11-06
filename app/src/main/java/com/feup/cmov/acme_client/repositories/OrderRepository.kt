package com.feup.cmov.acme_client.repositories

import androidx.lifecycle.LiveData
import com.feup.cmov.acme_client.utils.PreferencesUtils
import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.network.Result
import com.feup.cmov.acme_client.network.WebService
import com.feup.cmov.acme_client.network.requests.PlaceOrderRequest
import com.feup.cmov.acme_client.network.requests.SignupRequest
import com.feup.cmov.acme_client.network.responses.FetchOrdersResponse
import com.feup.cmov.acme_client.network.responses.PlaceOrderResponse
import com.feup.cmov.acme_client.network.responses.SignupResponse
import com.feup.cmov.acme_client.screens.main_menu.CartViewModel
import com.feup.cmov.acme_client.utils.Security
import com.feup.cmov.acme_client.utils.ShowFeedback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

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

    // Register user on the platform.
    suspend fun completeOrder(
        cartItems: Collection<CartViewModel.CartItem>,
        vouchers: Collection<Voucher>
    ): Result<PlaceOrderResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Create Request
                val request = PlaceOrderRequest(
                    cartItems,
                    vouchers
                )
                // Send HTTP request.
                val response: PlaceOrderResponse = webService.placeOrder(request)

                Result.Success(PlaceOrderResponse())
            } catch (e: Throwable) {
                when (e) {
                    is IOException -> Result.NetworkError
                    else -> Result.OtherError(e)
                }
            }
        }
    }
}