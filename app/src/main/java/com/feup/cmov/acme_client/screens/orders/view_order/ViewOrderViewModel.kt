package com.feup.cmov.acme_client.screens.orders.view_order

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.repositories.OrderRepository
import kotlinx.coroutines.launch

class ViewOrderViewModel @ViewModelInject constructor(
    val orderRepository: OrderRepository
) : ViewModel() {

    var orderDeleted = MutableLiveData<Boolean>(false)
    fun wasOrderDeleted() : LiveData<Boolean> = orderDeleted

    fun removeOrder(order: OrderWithItems) {
        viewModelScope.launch {
            orderRepository.removeOrder(order)
            orderDeleted.postValue(true)
        }
    }
}
