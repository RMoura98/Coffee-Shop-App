package com.feup.cmov.acme_terminal.database.models

data class OrderItem(
    val order_item_id: String,
    val quantity: Long,
    val price: Float,
    val order_id: String,
    val name: String,
    val item_id: Long
)
