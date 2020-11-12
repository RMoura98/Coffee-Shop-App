package com.feup.cmov.acme_terminal.database.models

data class ItemsWithInfo(
    val orderItem: OrderItem,
    val menuItem: MenuItem
)