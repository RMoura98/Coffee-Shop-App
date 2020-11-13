package com.feup.cmov.acme_client.screens.settings.payment_method

import android.text.TextUtils
import android.util.Patterns
import androidx.databinding.InverseMethod
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.forms.InvalidField
import com.feup.cmov.acme_client.network.Result
import com.feup.cmov.acme_client.repositories.UserRepository
import com.feup.cmov.acme_client.screens.signup.SignupViewModel
import kotlinx.coroutines.launch

class PaymentMethodViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var user = userRepository.getLoggedInUser()

    fun getUser(): LiveData<User> = user

    private var updateUserResult = MutableLiveData<UpdateUserResult>()

    fun getUpdateUserResult(): LiveData<UpdateUserResult> = updateUserResult

    fun updateFormatFields() {
        // Credit card
        val cardNumber = user.value!!.card_number
        val creditCardParts = arrayOf(cardNumber.subSequence(0, 4), cardNumber.subSequence(4, 8), cardNumber.subSequence(8, 12), cardNumber.subSequence(12, 16))
        user.value!!.card_number = TextUtils.join(" ", creditCardParts)
    }

    fun saveChanges() {
        val invalidFields = ArrayList<InvalidField>()

        validateInputFields(invalidFields)

        if (invalidFields.isNotEmpty()!!) {
            updateUserResult.postValue(UpdateUserResult.INVALID_FORM(invalidFields))
            return
        }
        viewModelScope.launch {
            val result: Result<Nothing?> = userRepository.updateUser(user.value!!)

            when (result) {
                is Result.Success -> updateUserResult.postValue(UpdateUserResult.SUCCESS)
                is Result.NetworkError -> updateUserResult.postValue(UpdateUserResult.NETWORK_ERROR)
                is Result.OtherError -> {
                    updateUserResult.postValue(UpdateUserResult.INVALID_FORM(invalidFields))
                    invalidFields.add(InvalidField(fieldName = "general", msg = result.msg))
                }
            }
        }
    }

    private fun validateInputFields(invalidFields: ArrayList<InvalidField>) {

        // >> NIF
        if (user.value!!.NIF.isBlank())
            invalidFields.add(InvalidField(fieldName = "NIF", msg = "Insert NIF."))
        else if (user.value!!.NIF.length != 9)
            invalidFields.add(InvalidField(fieldName = "NIF", msg = "Invalid NIF."))

        // >> Card Number
        if (user.value!!.card_number.isBlank())
            invalidFields.add(InvalidField(fieldName = "card_number", msg = "Insert card number."))
        else if (user.value!!.card_number.replace(" ", "").length != 16)
            invalidFields.add(InvalidField(fieldName = "card_number", msg = "Invalid card number."))

        // >> Card CVC
        if (user.value!!.card_cvc.isBlank())
            invalidFields.add(InvalidField(fieldName = "card_cvc", msg = "Insert card cvc."))

        // >> Card Expiration Date
        val expirationDateRegex = """(0[1-9]|10|11|12)/[0-9]{2}${'$'}""".toRegex()
        if (user.value!!.card_expiration.isBlank())
            invalidFields.add(
                InvalidField(
                    fieldName = "card_expiration",
                    msg = "Insert expiration date."
                )
            )
        else if (!expirationDateRegex.containsMatchIn(user.value!!.card_expiration))
            invalidFields.add(
                InvalidField(
                    fieldName = "card_expiration",
                    msg = "Invalid expiration date."
                )
            )
    }

    companion object {
        sealed class UpdateUserResult {
            data class INVALID_FORM(val invalidFields: List<InvalidField>) : UpdateUserResult()
            object NETWORK_ERROR : UpdateUserResult()
            object SUCCESS : UpdateUserResult()
        }
    }
}

object Converter {
    @InverseMethod("unformatCardNumber")
    @JvmStatic
    fun formatCardNumber(value: String?): String {
        if (value == null) return ""

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
        return value.replace(" ", "")
    }
}