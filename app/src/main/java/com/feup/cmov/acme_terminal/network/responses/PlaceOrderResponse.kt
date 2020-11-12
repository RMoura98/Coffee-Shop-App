package com.feup.cmov.acme_terminal.network.responses

import com.feup.cmov.acme_terminal.database.models.ItemsWithInfo
import com.feup.cmov.acme_terminal.database.models.OrderItem
import com.feup.cmov.acme_terminal.database.models.OrderWithItems
import com.feup.cmov.acme_terminal.database.models.Voucher
import com.google.gson.annotations.SerializedName
import java.util.*

data class PlaceOrderResponse (
        var order_id: String,
        var userId: String,
        var user: String,
        var order_sequential_id: Long,
        var createdAt: Date,
        var updatedAt: Date,
        var completed: Boolean,
        var total: Float,

        @SerializedName("orderItems")
    var orderItems: List<ItemsWithInfo>,

        @SerializedName("vouchers")
    var vouchers: List<Voucher>
)