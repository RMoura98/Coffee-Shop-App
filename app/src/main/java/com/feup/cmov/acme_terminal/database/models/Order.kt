package com.feup.cmov.acme_terminal.database.models

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class Order (
        val order_id: String,
        val userId: String,
        val user: String,
        val order_sequential_id: Long,
        val createdAt: Date,
        val updatedAt: Date,
        val completed: Boolean,
        val total: Float
) {
    fun formatCreationDate(): String {
        val df: DateFormat = SimpleDateFormat("dd/MM/YYYY HH:mm")
        return df.format(updatedAt)
    }

    fun formatCompletedDate(): String {
        val df: DateFormat = SimpleDateFormat("dd/MM/YYYY HH:mm")
        return df.format(createdAt)
    }
}