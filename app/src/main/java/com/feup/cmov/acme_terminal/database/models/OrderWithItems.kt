package com.feup.cmov.acme_terminal.database.models

import com.google.gson.Gson

data class OrderWithItems(
        val order: Order,
        val orderItems: List<ItemsWithInfo>,
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