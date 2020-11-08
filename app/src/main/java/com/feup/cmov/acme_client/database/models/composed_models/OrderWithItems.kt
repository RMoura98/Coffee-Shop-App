package com.feup.cmov.acme_client.database.models.composed_models

import androidx.room.Embedded
import androidx.room.Relation
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.OrderItem
import com.feup.cmov.acme_client.database.models.Voucher
import com.google.gson.Gson
import java.math.BigInteger
import java.security.MessageDigest
import java.text.DateFormat
import java.text.SimpleDateFormat

data class OrderWithItems(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "order_id",
        entityColumn = "order_id",
        entity = OrderItem::class
    )
    val orderItems: List<ItemsWithInfo>,
    @Relation(
        parentColumn = "order_id",
        entityColumn = "used_on_order_id",
        entity = Voucher::class
    )
    val vouchers: List<Voucher>
) {

    fun getNumberOfItemsBought(): Long {
        var quantity = 0L
        for(orderItem in orderItems) {
            quantity += orderItem.orderItem.quantity
        }
        return quantity
    }

    companion object {
        fun serialize(order: OrderWithItems): String {
            return Gson().toJson(order)
        }

        fun deserialize(order: String): OrderWithItems {
            return Gson().fromJson(order, OrderWithItems::class.java)
        }
    }
}