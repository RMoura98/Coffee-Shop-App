package com.feup.cmov.acme_client.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_table",
    indices = [Index(value = ["username"], unique = true), Index(value = ["uuid"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "password_hashed")
    var password_hashed: String,

    @ColumnInfo(name = "username")
    var userName: String,

    @ColumnInfo(name = "nif")
    var NIF: String,

    @ColumnInfo(name = "card_number")
    var card_number: String,

    @ColumnInfo(name = "card_cvc")
    var card_cvc: String,

    @ColumnInfo(name = "card_expiration")
    var card_expiration: String,

    @ColumnInfo(name = "phone_number")
    var phone_number: String
)
