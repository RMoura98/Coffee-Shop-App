package com.feup.cmov.acme_client.network.requests

import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.screens.checkout.CartViewModel

data class PlaceOrderRequest(
    val cartItems: Collection<CartViewModel.CartItem>,
    val vouchers: Collection<Voucher>
) {

}