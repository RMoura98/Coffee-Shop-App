package com.feup.cmov.acme_client.network.responses

import com.feup.cmov.acme_client.database.models.OrderItem
import com.feup.cmov.acme_client.database.models.Voucher
import com.google.gson.annotations.SerializedName

class OrderStatusResponse {
    @SerializedName("order_sequential_id")
    var order_sequential_id: Long = 0L

    @SerializedName("vouchers_received")
    lateinit var vouchers_received: List<Voucher>

    @SerializedName("order_items")
    lateinit var order_items: List<OrderItem>
}