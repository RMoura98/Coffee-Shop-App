package com.feup.cmov.acme_client.screens.orders

import android.view.View
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems

interface OrdersHistoryHandler {
    fun viewOrder(v: View, order: OrderWithItems)
}