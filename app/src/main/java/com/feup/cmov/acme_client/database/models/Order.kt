package com.feup.cmov.acme_client.database.models

import androidx.room.*
import com.feup.cmov.acme_client.database.type_converters.DateConverter
import java.util.*

@Entity(
    tableName = "order_table"
)
@TypeConverters(DateConverter::class)
data class Order (
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "createdAt")
    val createdAt: Date,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Date,

    @ColumnInfo(name = "userId")
    val userId: String
)
