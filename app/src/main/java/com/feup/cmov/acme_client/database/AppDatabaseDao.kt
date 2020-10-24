package com.feup.cmov.acme_client.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.Query
import com.feup.cmov.acme_client.database.models.User

@Dao
interface AppDatabaseDao {

    @Query("SELECT * FROM user_table WHERE userName = :userName LIMIT 1")
    fun loadUser(userName: String): LiveData<User>

    @Insert(onConflict = ABORT)
    fun createUser(user: User): Void
}
