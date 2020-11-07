package com.feup.cmov.acme_client.screens.checkout.cart

import android.view.View

interface CartHandler {
    fun onAddVoucherClick(v: View)
    fun placeOrder(v: View)
}