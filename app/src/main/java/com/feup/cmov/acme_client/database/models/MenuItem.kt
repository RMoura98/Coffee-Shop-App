package com.feup.cmov.acme_client.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "menu_item_table",
    indices = [Index(value = ["name"], unique = true)]
)
data class MenuItem (
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "imageName")
    val imageName: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "price")
    val price: Float
)
