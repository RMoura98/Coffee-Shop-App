package com.feup.cmov.acme_client.screens.signup;

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.User
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

    private var signupResult = MutableLiveData<SignupResult>()

    fun getSignupResult(): LiveData<SignupResult> = signupResult

    /**
     * Called from activity on login button click
     */
    fun performSignup() {

        val invalidFields = ArrayList<InvalidField>()

        // Empty fields.
        when {
            name.isBlank() -> invalidFields.add(InvalidField(fieldName="name", msg="Insert name."))
            NIF.isBlank() -> invalidFields.add(InvalidField(fieldName="NIF", msg="Insert NIF."))
            card_number.isBlank() -> invalidFields.add(InvalidField(fieldName="card_number", msg="Insert card number."))
            card_cvc.isBlank() -> invalidFields.add(InvalidField(fieldName="card_cvc", msg="Insert card cvc."))
            phone_number.isBlank() -> invalidFields.add(InvalidField(fieldName="phone_number", msg="Insert phone number."))
            card_expiration.isBlank() -> invalidFields.add(InvalidField(fieldName="card_expiration", msg="Insert card expiration date."))
            userName.isBlank() -> invalidFields.add(InvalidField(fieldName="username", msg="Insert username."))
            password.isBlank() -> invalidFields.add(InvalidField(fieldName="password", msg="Insert password."))
        }

        if (invalidFields.isNotEmpty()!!) {
            signupResult.postValue(SignupResult.INVALID_FORM(invalidFields))
            return
        }

        viewModelScope.launch {

            val result: Result<User> = appRepository.performSignup(name=name, NIF=NIF, card_number=card_number, card_cvc=card_cvc, card_expiration=card_expiration, phone_number=phone_number, userName=userName, password=password)

            when (result) {
                is Result.Success -> signupResult.postValue(SignupResult.SUCCESS(result.data))
                is Result.NetworkError -> signupResult.postValue(SignupResult.NETWORK_ERROR)
                is Result.OtherError -> {
                    signupResult.postValue(SignupResult.INVALID_FORM(invalidFields))
                    invalidFields.add(InvalidField(fieldName="general", msg=result.msg))
                }
            }
        }
    }

    companion object {
        /**
         * To pass signup result to fragment
         */
        sealed class SignupResult {
            data class INVALID_FORM(val invalidFields: List<InvalidField>): SignupResult()
            object NETWORK_ERROR: SignupResult()
            data class SUCCESS(val user: User) : SignupResult()
        }
    }

}
