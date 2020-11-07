package com.feup.cmov.acme_client.screens.orders

import android.view.View
import com.feup.cmov.acme_client.database.models.Order

interface OrdersHistoryHandler {
    fun viewOrder(v: View, orderId: String)
}