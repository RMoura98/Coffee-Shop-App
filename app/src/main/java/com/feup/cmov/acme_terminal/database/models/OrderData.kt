package com.feup.cmov.acme_terminal.database.models

data class OrderData (
        val order_id: String,
        val uuid: String,
        val orderItems: Map<Long, Long>,
        val vouchers: List<String>
)