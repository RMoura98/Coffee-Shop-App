package com.feup.cmov.acme_client.screens.signup;

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.forms.InvalidField
import com.feup.cmov.acme_client.network.Result
import com.feup.cmov.acme_client.network.responses.SignupResponse
import com.feup.cmov.acme_client.repositories.AppRepository
import com.feup.cmov.acme_client.screens.login.LoginViewModel
import kotlinx.coroutines.launch

class SignupViewModel @ViewModelInject constructor(private val appRepository: AppRepository): ViewModel() {

    /**
     * Two way bind-able fields
     */
    var name: String = ""
    var NIF: String = ""
    var card_number: String = ""
    var card_cvc: String = ""
    var card_expiration: String = ""
    var phone_number: String = ""
    var userName: String = ""
    var password: String = ""

    // For handling errors which are not related to any specific input field.
    var generalError: String = ""

    /**
     * To pass login result to activity
     */

    enum class SignupResult {
        INVALID_FORM, NETWORK_ERROR, SUCCESS
    }

    private var invalidFields = MutableLiveData<ArrayList<InvalidField>>()
    private var signupResult = MutableLiveData<SignupResult>()

    fun getSignupResult(): LiveData<SignupResult> = signupResult
    fun getInvalidFields(): LiveData<ArrayList<InvalidField>> = invalidFields

    /**
     * Called from activity on login button click
     */
    fun performSignup() {

        // Empty all errors.
        invalidFields.value = ArrayList()

        // Empty fields.
        when {
            name.isBlank() -> invalidFields.value?.add(InvalidField(fieldName="name", msg="Insert name."))
            NIF.isBlank() -> invalidFields.value?.add(InvalidField(fieldName="NIF", msg="Insert NIF."))
            card_number.isBlank() -> invalidFields.value?.add(InvalidField(fieldName="card_number", msg="Insert card number."))
            card_cvc.isBlank() -> invalidFields.value?.add(InvalidField(fieldName="card_cvc", msg="Insert card cvc."))
            phone_number.isBlank() -> invalidFields.value?.add(InvalidField(fieldName="phone_number", msg="Insert phone number."))
            card_expiration.isBlank() -> invalidFields.value?.add(InvalidField(fieldName="card_expiration", msg="Insert card expiration date."))
            userName.isBlank() -> invalidFields.value?.add(InvalidField(fieldName="username", msg="Insert username."))
            password.isBlank() -> invalidFields.value?.add(InvalidField(fieldName="password", msg="Insert password."))
        }

        if (invalidFields.value?.isNotEmpty()!!) {
            signupResult.postValue(SignupResult.INVALID_FORM)
            return
        }

        viewModelScope.launch {
            val result: Result<SignupResponse> = appRepository.performSignup(name=name, NIF=NIF, card_number=card_number, card_cvc=card_cvc, card_expiration=card_expiration, phone_number=phone_number, userName=userName, password=password)

            when (result) {
                is Result.Success -> signupResult.value = SignupResult.SUCCESS
                is Result.NetworkError -> signupResult.value = SignupResult.NETWORK_ERROR
                is Result.OtherError -> {
                    signupResult.postValue(SignupResult.INVALID_FORM)
                    invalidFields.value?.add(InvalidField(fieldName="general", msg=result.msg))
                }
            }
        }
    }

}
