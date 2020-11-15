package com.feup.cmov.acme_terminal.screens.order_history

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_terminal.database.models.OrderWithItems
import com.feup.cmov.acme_terminal.network.responses.PlaceOrderResponse
import com.feup.cmov.acme_terminal.repositories.OrderRepository
import com.feup.cmov.acme_terminal.utils.ShowFeedback
import kotlinx.coroutines.launch
import java.lang.Exception

class OrdersHistoryViewModel @ViewModelInject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private var allOrders = MutableLiveData<List<OrderWithItems>>()
    fun getAllOrdersLD() = allOrders

    fun getAllOrders() {
        viewModelScope.launch {
            try {
                val allOrdersRetrieved = orderRepository.getAllOrders() ?: throw Exception("No orders retrieved.")
                if (allOrders.value != allOrdersRetrieved)
                    allOrders.postValue(allOrdersRetrieved)
            } catch (e: Throwable) {
                ShowFeedback.makeSnackbar("Failed to fetch orders.")
            }
        }
    }
}
