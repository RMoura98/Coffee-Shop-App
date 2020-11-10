package com.feup.cmov.acme_client.screens.checkout

import android.util.Log
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.repositories.MenuRepository
import com.feup.cmov.acme_client.repositories.OrderRepository
import com.feup.cmov.acme_client.repositories.UserRepository
import com.feup.cmov.acme_client.repositories.VoucherRepository
import com.feup.cmov.acme_client.screens.checkout.cart.VoucherUsedAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.set
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow


class CartViewModel @ViewModelInject constructor(
    userRepository: UserRepository,
    menuRepository: MenuRepository,
    private val vouchersRepository: VoucherRepository,
    private val ordersRepository: OrderRepository
): ViewModel() {

    private var menuItems = menuRepository.getMenu()
    private var vouchers = vouchersRepository.getAllVouchers()

    private val selectedVouchers = MutableLiveData(ArrayList<Voucher>())
    private var totalCartItems = MutableLiveData(0)
    private var subtotalCartPrice = MutableLiveData(0f)
    private var totalCartPrice = MutableLiveData(0f)
    private var totalSavings = MutableLiveData(0f)
    private val cartListLiveData = MutableLiveData<MutableMap<Long, CartItem>>()
    private val placedOrder = MutableLiveData<OrderWithItems?>()
    private val loggedInUser = userRepository.getLoggedInUser()

    var isLoading = ObservableField<Boolean>(false)
    private val cartList = mutableMapOf<Long, CartItem>()

    fun getMenuItems(): LiveData<List<MenuItem>> = menuItems
    fun getVouchers(): LiveData<List<Voucher>> = vouchers
    fun getSelectedVouchers(): LiveData<ArrayList<Voucher>> = selectedVouchers

    fun getTotalCartItems() : LiveData<Int> = totalCartItems
    fun getTotalPrice() : LiveData<Float>  = totalCartPrice
    fun getSubtotalCartPrice() : LiveData<Float> = subtotalCartPrice
    fun getTotalSavings() : LiveData<Float>  = totalSavings
    fun getLoggedInUser(): LiveData<User> = loggedInUser

    fun getPlacedOrder(): LiveData<OrderWithItems?> = placedOrder

    data class CartItem(val item: MenuItem, var quantity: Int = 1) {
        fun plus(other: Int) {
            quantity += other
        }
    }
    fun getCartListLiveData(): LiveData<MutableMap<Long, CartItem>> = cartListLiveData

    fun addItemToCart(item: MenuItem) {

        if (!cartList.containsKey(item.id))
            cartList[item.id] = CartItem(item)
        else
            cartList[item.id]!!.plus(1)

        // Log.e("CartViewModel", "Posted value")
        cartListLiveData.postValue(cartList)

        totalCartItems.value = totalCartItems.value!! + 1
        totalCartItems.postValue(totalCartItems.value!!)
        subtotalCartPrice.value = subtotalCartPrice.value!! + item.price
        subtotalCartPrice.postValue(subtotalCartPrice.value!!)
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

    fun updateTotalSavings() {
        var savings = 0f

        val free_coffee_vouchers = countCoffeVouchersSelected()
        val free_item_vouchers = if(isAnyDiscountVoucherSelected()) 1 else 0
        val coffee_price = getCoffePrice()

        // Apply free coffe voucher
        if(free_coffee_vouchers > 0)
            savings += coffee_price!! * free_coffee_vouchers

        // Apply discount vouchers.
        var discountSavings = (getSubtotalCartPrice().value!! - savings) - (0.95f).pow(free_item_vouchers) * (getSubtotalCartPrice().value!! - savings)
        discountSavings = floor(discountSavings * 100) / 100 // round to floor with 2 decimal places
        savings += discountSavings
        totalSavings.value = savings
        totalSavings.postValue(savings)
        Log.e("totalSavings", totalSavings.value!!.toString())
        Log.e("free_item_vouchers", free_item_vouchers.toString())
    }

    fun updateTotalPrice() {
        totalCartPrice.value = subtotalCartPrice.value?.minus(totalSavings.value!!)
        if (totalCartPrice.value!! < 0f) totalCartPrice.value = 0f
        totalCartPrice.postValue(totalCartPrice.value)
    }

    fun getSavingsForSelectedVouchers() : List<VoucherUsedAdapter.VoucherWithSavings> {
        val voucherList = ArrayList<VoucherUsedAdapter.VoucherWithSavings>()
        for (voucher in selectedVouchers.value!!) {
            if(voucher.voucherType == "free_coffee")
                voucherList.add(VoucherUsedAdapter.VoucherWithSavings(voucher, getCoffePrice()!!))
            else if(voucher.voucherType == "discount") {
                var savings = (getSubtotalCartPrice().value!! - (getCoffePrice() ?: 0f) * countCoffeVouchersSelected()) * 0.05F
                savings = floor(savings * 100) / 100 // round to floor with 2 decimal places
                voucherList.add(VoucherUsedAdapter.VoucherWithSavings(voucher, savings))
            }
        }
        return voucherList
    }

    // Returns `true` if order was successfully placed; false otherwise.
    fun placeOrder() {
        // Prevent user from pacing empty order
        if(cartList.isEmpty())
            return
        // Prevent user from clicking button multiple times.
        if(isLoading.get()!!)
            return
        viewModelScope.launch {
            isLoading.set(true)
            delay(500)
            val order = ordersRepository.placeOrder(cartList.values, selectedVouchers.value!!, subtotalCartPrice.value!! - totalSavings.value!!)
            placedOrder.postValue(order)
            isLoading.set(false)
        }
    }

    fun clearViewModel() {
        vouchers = vouchersRepository.getAllVouchers()
        selectedVouchers.postValue(ArrayList())
        totalCartItems.postValue(0)
        subtotalCartPrice.postValue(0f)
        cartList.clear()
        cartListLiveData.postValue(cartList)
        placedOrder.postValue(null)
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                totalCartItems.value = 0
                subtotalCartPrice.value = 0f
            }
        }
    }

    fun updateCartItem(cartItem: CartItem, originalQuantity: Int) {
        var id = cartItem.item.id

        // < 0 == reducing items quantity | > 0 == increasing items quantity
        var quantityDifference = cartList[id]!!.quantity - originalQuantity
        if (quantityDifference == 0) return

        // Check Voucher List for inconsistencies with coffee (Free Item)
        // Only if the item is coffee and if the user is reducing the item quantity
        // if there is Vouchers in excess -> update selectedVouchers (by removing vouchers)
        if (cartItem.item.name == "Coffee" && quantityDifference < 0) {
            var coffeeVouchersSelected = selectedVouchers.value!!.filter { it.voucherType == "free_coffee" }
            var coffeeVouchersInExcess = coffeeVouchersSelected.count() - cartItem.quantity
            if (coffeeVouchersInExcess > 0) {
                for(i in 0 until coffeeVouchersInExcess)
                    selectedVouchers.value!!.remove(coffeeVouchersSelected[i])
            }
        }

        // Update cartList
        if (cartItem.quantity == 0)
            cartList.remove(id)
        cartListLiveData.postValue(cartList)

        if(cartList.isEmpty()) {
            selectedVouchers.value = ArrayList()
            selectedVouchers.postValue(selectedVouchers.value)
        }

        // Update Subtotal Price and Total Number of Items
        totalCartItems.value = totalCartItems.value!! + quantityDifference
        totalCartItems.postValue(totalCartItems.value)
        subtotalCartPrice.value = subtotalCartPrice.value!! + (quantityDifference * cartItem.item.price)
        subtotalCartPrice.postValue(subtotalCartPrice.value)

        //Update Total Price and Total Savings
        updateTotalSavings()
        updateTotalPrice()

        notifyVoucherChanges()
    }

}
