package com.feup.cmov.acme_client.screens.settings.payment_method

import android.text.Editable
import android.view.View

interface PaymentMethodHandler {
    fun onSaveButtonClick(v: View)
    fun afterTextChangedCardNumber(s: Editable)
    fun afterTextChangedCardExpiration(s: Editable)
}