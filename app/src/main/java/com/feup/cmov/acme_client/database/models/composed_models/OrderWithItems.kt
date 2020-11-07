package com.feup.cmov.acme_client.database.models.composed_models

import androidx.room.Embedded
import androidx.room.Relation
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.OrderItem
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
    val orderItems: List<ItemsWithInfo>
) {

    fun getNumberOfItemsBought(): Long {
        var quantity = 0L
        for(orderItem in orderItems) {
            quantity += orderItem.orderItem.quantity
        }
        return quantity
    }
}