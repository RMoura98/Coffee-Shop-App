package com.feup.cmov.acme_client.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.feup.cmov.acme_client.database.models.*

@Database(entities = [User::class, MenuItem::class, Voucher::class, Order::class, OrderItem::class], version = 25, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val appDatabaseDao: AppDatabaseDao

}