package com.feup.cmov.acme_client.screens.signup
import android.text.Editable
import android.view.View

interface SignupHandler {
    fun onSubmitButtonClick(v: View)
    fun afterTextChangedCardNumber(s: Editable)
    fun afterTextChangedCardExpiration(s: Editable)

}