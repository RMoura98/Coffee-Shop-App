package com.feup.cmov.acme_client.screens.settings.payment_method

import android.text.TextUtils
import androidx.databinding.InverseMethod
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.repositories.UserRepository
import kotlinx.coroutines.launch

class PaymentMethodViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var user = userRepository.getLoggedInUser()

    fun getUser(): LiveData<User> = user

    fun updateFormatFields() {
        // Credit card
        val cardNumber = user.value!!.card_number
        val creditCardParts = arrayOf(cardNumber.subSequence(0, 4), cardNumber.subSequence(4, 8), cardNumber.subSequence(8, 12), cardNumber.subSequence(12, 16))
        user.value!!.card_number = TextUtils.join(" ", creditCardParts)
    }

    fun saveChanges() {
        viewModelScope.launch {
            userRepository.updateUser(user.value!!)
        }
    }
}

object Converter {
    @InverseMethod("unformatCardNumber")
    @JvmStatic
    fun formatCardNumber(value: String?): String {
        println("Card number: $value")
        if (value == null) return ""
        println("Formatting: $value")

        val creditCardParts = arrayOf(
            value.subSequence(0, 4),
            value.subSequence(4, 8),
            value.subSequence(8, 12),
            value.subSequence(12, 16)
        )

        return TextUtils.join(" ", creditCardParts)
    }

    @JvmStatic
    fun unformatCardNumber(value: String): String {
        println("Unformatting: $value")
        return value.replace(" ", "")
    }
}