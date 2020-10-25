package com.feup.cmov.acme_client.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_table", indices = [Index(value = ["username"], unique = true)])
data class User (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "username")
    val userName: String,

    @ColumnInfo(name = "nif")
    val NIF: String,

    @ColumnInfo(name = "card_number")
    val card_number: String,

    @ColumnInfo(name = "card_cvc")
    val card_cvc: String,

    @ColumnInfo(name = "card_expiration")
    val card_expiration: String,

    @ColumnInfo(name = "phone_number")
    val phone_number: String
)
