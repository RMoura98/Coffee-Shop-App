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
        parentColumn = "id",
        entityColumn = "order_id",
        entity = OrderItem::class
    )
    val orderItems: List<ItemsWithInfo>
) {
    fun formatCreationDate(): String {
        val df: DateFormat = SimpleDateFormat("dd/MM/YYYY HH:mm")
        return df.format(order.updatedAt)
    }

    fun formatCompletedDate(): String {
        val df: DateFormat = SimpleDateFormat("dd/MM/YYYY HH:mm")
        return df.format(order.createdAt)
    }

    fun getNumberOfItemsBought(): Long {
        var quantity = 0L
        for(orderItem in orderItems) {
            quantity += orderItem.orderItem.quantity
        }
        return quantity
    }

    fun getTotalPriceAfterDiscount(): Float {
        var price = 0f
        for(orderItem in orderItems) {
            price += orderItem.orderItem.priceAfterDiscounts
        }
        return price
    }

    fun getOrderNumber(): String {
        val hash = MessageDigest.getInstance("MD5").digest(BigInteger.valueOf(order.id).toByteArray())
        val hashAsString = BigInteger(1, hash).toString(24)
        return hashAsString.substring(0, 4).toUpperCase()
    }
}