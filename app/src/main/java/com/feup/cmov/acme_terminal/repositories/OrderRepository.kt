package com.feup.cmov.acme_terminal.repositories

import android.util.Log
import com.feup.cmov.acme_terminal.database.models.Order
import com.feup.cmov.acme_terminal.network.WebService
import com.feup.cmov.acme_terminal.network.requests.PlaceOrderRequest
import com.feup.cmov.acme_terminal.network.responses.PlaceOrderResponse
import javax.inject.Inject

class OrderRepository
@Inject constructor(
    private val webService: WebService
) {
    suspend fun placeOrder(order: Order, signature: String) {
        try {
            val request = PlaceOrderRequest(order.orderItems, order.uuid, order.order_id, order.vouchers)
            val response = webService.placeOrder(request, signature)
        } catch (e: Throwable) {
            Log.e("OrderRepository", "placeOrder: $e")
        }
    }
}