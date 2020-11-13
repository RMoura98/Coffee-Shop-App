package com.feup.cmov.acme_terminal.repositories

import android.util.Log
import com.feup.cmov.acme_terminal.database.models.Order
import com.feup.cmov.acme_terminal.database.models.OrderData
import com.feup.cmov.acme_terminal.database.models.OrderWithItems
import com.feup.cmov.acme_terminal.network.WebService
import com.feup.cmov.acme_terminal.network.requests.PlaceOrderRequest
import com.feup.cmov.acme_terminal.network.responses.PlaceOrderResponse
import javax.inject.Inject

class OrderRepository
@Inject constructor(
    private val webService: WebService
) {
    suspend fun placeOrder(order: OrderData, signature: String): PlaceOrderResponse? {
        return try {
            val request = PlaceOrderRequest(order.orderItems, order.uuid, order.order_id, order.vouchers)
            val response = webService.placeOrder(request, signature)
            response
        } catch (e: Throwable) {
            Log.e("OrderRepository", "placeOrder: $e")
            null
        }

    }

    suspend fun getAllOrders(): List<OrderWithItems>? {
        return try {
            val response = webService.getAllOrders()

            var orderWithItems = response.map {
                OrderWithItems(Order(order_id=it.order_id, userId=it.userId, user=it.user, order_sequential_id=it.order_sequential_id,
                    createdAt=it.createdAt, updatedAt=it.updatedAt, completed=it.completed, total=it.total), it.orderItems, it.vouchers)
            }
            orderWithItems
        } catch (e: Throwable) {
            Log.e("OrderRepository", "getAllOrders: $e")
            null
        }

    }

}