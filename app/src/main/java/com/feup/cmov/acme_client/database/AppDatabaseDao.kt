package com.feup.cmov.acme_client.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.*
import com.feup.cmov.acme_client.database.models.*
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems

@Dao
interface AppDatabaseDao {

    // Users
    @Query("SELECT * FROM user_table WHERE userName = :userName LIMIT 1")
    fun loadUser(userName: String): User?

    @Query("SELECT * FROM user_table WHERE userName = :userName LIMIT 1")
    fun loadUserAsync(userName: String): LiveData<User>

    @Insert(onConflict = ABORT)
    fun createUser(user: User): Void

    // Menu
    @Query("SELECT * FROM menu_item_table")
    fun getMenu(): LiveData<List<MenuItem>>

    @Insert(onConflict = REPLACE)
    fun createMenuItem(menuItems: List<MenuItem>): Void

    // Vouchers
    @Query("SELECT * FROM voucher_table WHERE user_id = :userId AND used = 0")
    fun getUnusedVouchers(userId: String): LiveData<List<Voucher>>

    @Query("SELECT * FROM voucher_table WHERE user_id = :userId")
    fun getAllVouchers(userId: String): LiveData<List<Voucher>>

    @Insert(onConflict = IGNORE)
    fun createVouchers(vouchers: List<Voucher>): Void

    // Orders
    @Transaction
    @Query("SELECT * FROM order_table WHERE userId = :userId")
    fun getOrdersWithItems(userId: String): LiveData<List<OrderWithItems>>

    @Insert(onConflict = REPLACE)
    fun createOrders(vouchers: List<Order>): Void

    @Insert(onConflict = REPLACE)
    fun createOrderItems(vouchers: List<OrderItem>): Void
}
