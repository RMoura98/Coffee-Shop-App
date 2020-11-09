package com.feup.cmov.acme_terminal.screens.order_details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.feup.cmov.acme_terminal.database.models.Order
import com.feup.cmov.acme_terminal.repositories.OrderRepository

class OrderDetailsViewModel @ViewModelInject constructor(
        orderRepository: OrderRepository
) : ViewModel() {

    var order: MutableLiveData<Order> = MutableLiveData()
}