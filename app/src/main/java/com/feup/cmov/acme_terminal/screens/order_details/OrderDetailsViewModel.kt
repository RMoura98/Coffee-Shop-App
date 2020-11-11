package com.feup.cmov.acme_terminal.screens.order_details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_terminal.database.models.Order
import com.feup.cmov.acme_terminal.repositories.OrderRepository
import kotlinx.coroutines.launch

class OrderDetailsViewModel @ViewModelInject constructor(
        private val orderRepository: OrderRepository
) : ViewModel() {

    var order: MutableLiveData<Order> = MutableLiveData()

    fun placeOrder(order: Order, signature: String) {
        this.order.postValue(order)

        viewModelScope.launch {
            orderRepository.placeOrder(order, signature)
        }
    }
}