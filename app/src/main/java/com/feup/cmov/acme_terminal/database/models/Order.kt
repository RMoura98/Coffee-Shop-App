package com.feup.cmov.acme_terminal.database.models

data class Order (
    val order_id: String,
    val uuid: String,
    val orderItems: Map<Long, Long>,
    val vouchers: List<String>
)