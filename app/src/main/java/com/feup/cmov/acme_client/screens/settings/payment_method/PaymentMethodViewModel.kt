package com.feup.cmov.acme_client.screens.settings.payment_method

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.repositories.UserRepository

class PaymentMethodViewModel @ViewModelInject constructor(
    userRepository: UserRepository
) : ViewModel() {

    var creditCardFormatted = ""

    private var user = userRepository.getLoggedInUser()

    fun getUser(): LiveData<User> = user

    fun updateFormatFields() {
        // Credit card
        val credit_card_unformat = user.value!!.card_number
        val credit_card_parts = arrayOf(credit_card_unformat.subSequence(0, 4), credit_card_unformat.subSequence(4, 8), credit_card_unformat.subSequence(8, 12), credit_card_unformat.subSequence(12, 16))
        creditCardFormatted = TextUtils.join(" ", credit_card_parts)
    }

}