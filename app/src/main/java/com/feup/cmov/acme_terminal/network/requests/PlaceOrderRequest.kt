package com.feup.cmov.acme_terminal.network.requests

data class PlaceOrderRequest (
    val orderItems: Map<Long, Long>,
    val uuid: String,
    val order_id: String,
    val vouchers: List<String>
)