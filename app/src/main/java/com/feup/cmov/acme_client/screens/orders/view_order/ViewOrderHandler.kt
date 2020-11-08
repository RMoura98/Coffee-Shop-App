package com.feup.cmov.acme_client.screens.orders.view_order

import android.view.View

interface ViewOrderHandler {
    fun clickPickupOrder(v: View)
    fun clickOrderReceipt(v: View)
    fun clickRemakeOrder(v: View)
    fun clickDeleteOrder(v: View)
}