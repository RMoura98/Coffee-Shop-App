package com.feup.cmov.acme_client.database.models

import androidx.room.*
import com.feup.cmov.acme_client.database.type_converters.DateConverter
import com.google.gson.Gson
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity(
    tableName = "order_table"
)
@TypeConverters(DateConverter::class)
data class Order (
    @PrimaryKey
    val order_id: String,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "order_sequential_id")
    val order_sequential_id: Long,

    @ColumnInfo(name = "createdAt")
    val createdAt: Date,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Date,

    @ColumnInfo(name = "completed")
    val completed: Boolean,

    @ColumnInfo(name = "total")
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