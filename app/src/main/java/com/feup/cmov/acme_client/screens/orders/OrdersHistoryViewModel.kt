package com.feup.cmov.acme_client.screens.orders

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.repositories.OrderRepository
import com.feup.cmov.acme_client.repositories.UserRepository


class OrdersHistoryViewModel @ViewModelInject constructor(
    orderRepository: OrderRepository
) : ViewModel() {

    private var orders = orderRepository.getOrders()
    fun getOrders(): LiveData<List<OrderWithItems>> = orders
}
