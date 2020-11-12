package com.feup.cmov.acme_terminal.database.models

data class MenuItem (
    val id: Long,
    val name: String,
    val description: String,
    val imageName: String,
    val category: String,
    val price: Float
)
