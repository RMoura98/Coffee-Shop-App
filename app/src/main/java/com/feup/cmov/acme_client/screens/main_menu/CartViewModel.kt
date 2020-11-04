package com.feup.cmov.acme_client.screens.main_menu

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.repositories.MenuRepository
import kotlin.collections.set


class CartViewModel @ViewModelInject constructor(
    menuRepository: MenuRepository
): ViewModel() {

    private var menuItems = menuRepository.getMenu()
    fun getMenuItems(): LiveData<List<MenuItem>> = menuItems

    fun getTotalCartItems() : LiveData<Int> = totalCartItems
    fun getTotalCartPrice() : LiveData<Float> = totalCartPrice

    data class CartList(val item: MenuItem, var quantity: Int = 1) {
        fun plus(other: Int) {
            quantity += other
        }
    }
    private var cartList = mutableMapOf<Long, CartList>()   // itemId -> (item, quantity)

    fun addItemToCart(item: MenuItem) {
        if (cartList.containsKey(item.id))
            cartList[item.id] = CartList(item)
        else
            cartList[item.id]?.plus(1)

        Log.d("Added to cart ID: ", item.id.toString())

        totalCartItems.postValue(totalCartItems.value!! + 1)
        totalCartPrice.postValue(totalCartPrice.value!! + item.price)

        Log.d("Cart List: ", cartList.toString())
        Log.d("totalCartItems: ", totalCartItems.toString())
        Log.d("totalCartPrice: ", totalCartPrice.toString())

    }

    fun getCartList(): MutableMap<Long,CartList> {
        return cartList
    }

    companion object {
        private var totalCartItems = MutableLiveData(0)
        private var totalCartPrice = MutableLiveData(0f)
    }
}
