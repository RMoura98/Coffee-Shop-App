package com.feup.cmov.acme_client.database.models

import androidx.room.*

@Entity(
    tableName = "order_item_table"
)
data class OrderItem(
    @PrimaryKey
    val order_item_id: String,

    @ColumnInfo(name = "quantity")
    val quantity: Long,

    @ColumnInfo(name = "price")
    val price: Float,

    @ColumnInfo(name = "order_id")
    val order_id: String,

    @ColumnInfo(name = "item_id")
    val item_id: Long
)
