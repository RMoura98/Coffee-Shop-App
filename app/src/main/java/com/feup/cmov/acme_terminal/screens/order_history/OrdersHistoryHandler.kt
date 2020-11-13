package com.feup.cmov.acme_terminal.screens.order_history

import android.view.View
import com.feup.cmov.acme_terminal.database.models.OrderWithItems

interface OrdersHistoryHandler {
    fun viewOrder(v: View, order: OrderWithItems)
}