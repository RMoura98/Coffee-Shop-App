package com.feup.cmov.acme_client.network.responses

import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.OrderItem
import com.google.gson.annotations.SerializedName


class FetchOrdersResponse {
    @SerializedName("orders")
    lateinit var orders: List<Order>

    @SerializedName("orderItems")
    lateinit var orderItems: List<OrderItem>
}