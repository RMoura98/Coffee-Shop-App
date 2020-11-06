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
import com.feup.cmov.acme_client.screens.main_menu.cart.VoucherUsedAdapter
import com.feup.cmov.acme_client.utils.ShowFeedback
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.set
import kotlin.math.pow


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

    fun isVoucherSelected(voucher: Voucher): Boolean {
        return selectedVouchers.value!!.contains(voucher)
    }

    fun isCoffeeInTheCart(): Boolean {
        val cartItems = cartListLiveData.value!!.values
        return cartItems.any { it.item.name == "Coffee" }
    }

    fun getCoffePrice(): Float? {
        if(!isCoffeeInTheCart())
            return null
        val cartItems = cartListLiveData.value!!.values
        return cartItems.filter { it.item.name == "Coffee" }[0].item.price
    }

    fun countCoffeVouchersSelected(): Long {
        return selectedVouchers.value!!.count { it.voucherType == "free_coffee" }.toLong()
    }

    fun countCoffesInCart(): Long {
        val cartItems = cartListLiveData.value!!.values
        val coffeInCart = cartItems.filter { it.item.name == "Coffee" }
        if(coffeInCart.isNotEmpty())
            return coffeInCart[0].quantity.toLong()
        else
            return 0L
    }

    fun isAnyDiscountVoucherSelected(): Boolean {
        return selectedVouchers.value!!.any { it.voucherType == "discount" }
    }

    private fun notifyVoucherChanges() {
        selectedVouchers.postValue(selectedVouchers.value)
    }

    fun getTotalSavings(): Float {
        var savings = 0f

        val free_coffee_vouchers = countCoffeVouchersSelected()
        val free_item_vouchers = if(isAnyDiscountVoucherSelected()) 1 else 0
        val coffee_price = getCoffePrice()

        // Apply free coffe voucher
        savings += coffee_price!! * free_coffee_vouchers

        // Apply discount vouchers.
        savings += (getTotalCartPrice().value!! - savings) - (0.95f).pow(free_item_vouchers) * (getTotalCartPrice().value!! - savings)

        return savings
    }

    fun getSavingsForSelectedVouchers() : List<VoucherUsedAdapter.VoucherWithSavings> {
        val voucherList = ArrayList<VoucherUsedAdapter.VoucherWithSavings>()
        for (voucher in selectedVouchers.value!!) {
            if(voucher.voucherType == "free_coffee")
                voucherList.add(VoucherUsedAdapter.VoucherWithSavings(voucher, getCoffePrice()!!))
            else if(voucher.voucherType == "discount") {
                val savings = (getTotalCartPrice().value!! - getCoffePrice()!! * countCoffeVouchersSelected()) * 0.05F
                voucherList.add(VoucherUsedAdapter.VoucherWithSavings(voucher, savings))
            }
        }
        return voucherList
    }
}
