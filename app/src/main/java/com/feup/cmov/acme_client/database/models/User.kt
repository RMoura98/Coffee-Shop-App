package com.feup.cmov.acme_client.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_table", indices = [Index(value = ["username"], unique = true)])
data class User (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "username")
    val userName: String = "",

    @ColumnInfo(name = "nif")
    val NIF: String = ""
)
