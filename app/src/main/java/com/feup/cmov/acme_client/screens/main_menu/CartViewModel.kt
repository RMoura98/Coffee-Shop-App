package com.feup.cmov.acme_client.screens.main_menu

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.repositories.MenuRepository
import com.feup.cmov.acme_client.repositories.VoucherRepository
import com.feup.cmov.acme_client.utils.ShowFeedback
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.set


class CartViewModel @ViewModelInject constructor(
    menuRepository: MenuRepository,
    vouchersRepository: VoucherRepository
): ViewModel() {

    private var menuItems = menuRepository.getMenu()
    private var vouchers = vouchersRepository.getAllVouchers()

    private val selectedVouchers = MutableLiveData(ArrayList<Voucher>())
    private var totalCartItems = MutableLiveData(0)
    private var totalCartPrice = MutableLiveData(0f)
    private val cartListLiveData = MutableLiveData<MutableMap<Long, CartItem>>()

    fun getMenuItems(): LiveData<List<MenuItem>> = menuItems
    fun getVouchers(): LiveData<List<Voucher>> = vouchers
    fun getSelectedVouchers(): LiveData<ArrayList<Voucher>> = selectedVouchers

    fun getTotalCartItems() : LiveData<Int> = totalCartItems
    fun getTotalCartPrice() : LiveData<Float> = totalCartPrice

    data class CartItem(val item: MenuItem, var quantity: Int = 1) {
        fun plus(other: Int) {
            quantity += other
        }
    }
    private val cartList = mutableMapOf<Long, CartItem>()
    fun getCartListLiveData(): LiveData<MutableMap<Long, CartItem>> = cartListLiveData

    fun addItemToCart(item: MenuItem) {

        if (!cartList.containsKey(item.id))
            cartList[item.id] = CartItem(item)
        else
            cartList[item.id]!!.plus(1)

        // Log.e("CartViewModel", "Posted value")
        cartListLiveData.postValue(cartList)

        Log.d("Added to cart ID: ", item.id.toString())

        totalCartItems.postValue(totalCartItems.value!! + 1)
        totalCartPrice.postValue(totalCartPrice.value!! + item.price)

        Log.d("Cart List: ", cartList.toString())
        Log.d("totalCartItems: ", totalCartItems.toString())
        Log.d("totalCartPrice: ", totalCartPrice.toString())
    }

    fun selectVoucher(voucher: Voucher) {
        selectedVouchers.value!!.add(voucher)
        notifyVoucherChanges()
    }

    fun unselectVoucher(voucher: Voucher) {
        selectedVouchers.value!!.remove(voucher)
        notifyVoucherChanges()
    }

    private fun notifyVoucherChanges() {
        selectedVouchers.postValue(selectedVouchers.value)
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                delay(200)
                ShowFeedback.makeSnackbar("New order total: 25.63€", Snackbar.LENGTH_SHORT)
            }
        }
    }
}
