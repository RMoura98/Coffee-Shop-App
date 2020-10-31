package com.feup.cmov.acme_client.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.OnConflictStrategy.REPLACE
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.database.models.Voucher
import dagger.hilt.android.qualifiers.ApplicationContext

@Dao
interface AppDatabaseDao {

    @Query("SELECT * FROM user_table WHERE userName = :userName LIMIT 1")
    fun loadUser(userName: String): User?

    @Query("SELECT * FROM user_table WHERE userName = :userName LIMIT 1")
    fun loadUserAsync(userName: String): LiveData<User>

    @Insert(onConflict = ABORT)
    fun createUser(user: User): Void

    @Query("SELECT * FROM menu_item_table")
    fun getMenu(): LiveData<List<MenuItem>>

    @Insert(onConflict = REPLACE)
    fun createMenuItem(menuItems: List<MenuItem>): Void

    @Query("SELECT * FROM voucher_table WHERE user_id = :userId AND used = 0")
    fun getUnusedVouchers(userId: String): LiveData<List<Voucher>>

    @Insert(onConflict = REPLACE)
    fun createVouchers(vouchers: List<Voucher>): Void

    @Query("DELETE FROM voucher_table")
    fun deleteAllVouchers()

    @Transaction
    fun deleteCreateVouchers(vouchers: List<Voucher>) {
        deleteAllVouchers()
        createVouchers(vouchers)
    }
}
