package com.feup.cmov.acme_terminal.screens.order_details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_terminal.database.models.Order
import com.feup.cmov.acme_terminal.database.models.OrderData
import com.feup.cmov.acme_terminal.database.models.OrderWithItems
import com.feup.cmov.acme_terminal.network.responses.PlaceOrderResponse
import com.feup.cmov.acme_terminal.repositories.OrderRepository
import kotlinx.coroutines.launch

class OrderDetailsViewModel @ViewModelInject constructor(
        private val orderRepository: OrderRepository
) : ViewModel() {

    var order: MutableLiveData<OrderWithItems> = MutableLiveData()

    fun placeOrder(orderData: OrderData, signature: String) {


        viewModelScope.launch {
            val orderResponse: PlaceOrderResponse = orderRepository.placeOrder(orderData, signature)!!
            var orderData: Order = Order(
                orderResponse.order_id,
                orderResponse.userId,
                orderResponse.user,
                orderResponse.order_sequential_id,
                orderResponse.createdAt,
                orderResponse.updatedAt,
                orderResponse.completed,
                orderResponse.total
            )

            var orderWithItems = OrderWithItems(orderData,  orderResponse.orderItems, orderResponse.vouchers)


            order.value = orderWithItems
            order.postValue(orderWithItems)
        }
    }
}