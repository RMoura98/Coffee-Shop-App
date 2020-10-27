package com.feup.cmov.acme_client.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.User

@Database(entities = [User::class, MenuItem::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val appDatabaseDao: AppDatabaseDao

}