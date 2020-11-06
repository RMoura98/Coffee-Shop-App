package com.feup.cmov.acme_client.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "voucher_table"
)
data class Voucher(
    @PrimaryKey
    val voucherId: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "voucher_type")
    val voucherType: String,

    @ColumnInfo(name = "used")
    val used: Boolean
)