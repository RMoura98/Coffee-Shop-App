package com.feup.cmov.acme_client.database.models

import androidx.room.*

@Entity(
    tableName = "order_item_table"
)
data class OrderItem(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "quantity")
    val quantity: Long,

    @ColumnInfo(name = "price_before_discounts")
    val priceBeforeDiscounts: Float,

    @ColumnInfo(name = "price_after_discounts")
    val priceAfterDiscounts: Float,

    @ColumnInfo(name = "order_id")
    val order_id: Long,

    @ColumnInfo(name = "item_id")
    val item_id: Long
)
