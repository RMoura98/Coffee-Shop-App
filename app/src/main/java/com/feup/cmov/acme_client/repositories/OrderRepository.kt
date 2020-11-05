package com.feup.cmov.acme_client.repositories

import androidx.lifecycle.LiveData
import com.feup.cmov.acme_client.utils.PreferencesUtils
import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.network.WebService
import com.feup.cmov.acme_client.network.responses.FetchOrdersResponse
import com.feup.cmov.acme_client.utils.ShowFeedback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
}