package com.feup.cmov.acme_client.screens.main_menu

import android.util.Log
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.repositories.MenuRepository
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import kotlin.collections.set


class CartViewModel @ViewModelInject constructor(
    menuRepository: MenuRepository
): ViewModel() {

    private var menuItems = menuRepository.getMenu()
    fun getMenuItems(): LiveData<List<MenuItem>> = menuItems

    var cartList = mutableMapOf<Long, Int>() // (ID, QUANTITY)
    var totalCartItems: Int = 0

    fun getCartItems(): List<MenuItem> {
//        var menuItemList = menuItems.value
//        return menuItemList?.filter { it.id in cartList.keys }!!
        return ArrayList<MenuItem>()
    }

    fun addItemToCart(itemId: Long) {
        cartList[itemId] = cartList.getOrElse<Long, Int>(itemId, { 0 }) + 1
        Log.d("Added to cart ID: ", itemId.toString())

        totalCartItems++

        Log.d("Cart List: ", cartList.toString())
        Log.d("totalCartItems: ", totalCartItems.toString())
    }

}
