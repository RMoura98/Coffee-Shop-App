package com.feup.cmov.acme_client.screens.signup;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel : ViewModel() {

    /**
     * Two way bind-able fields
     */
    var name: String = ""
    var NIF: String = ""
    var card_number: String = ""
    var card_cvc: String = ""
    var card_expiration: String = ""
    var phone_number: String = ""
    var username: String = ""
    var password: String = ""

    /**
     * To pass login result to activity
     */

    enum class SignupResults {
        INVALID, SUCCESS
    }

    private val signupResult = MutableLiveData<SignupResults>()

    fun getSignupResult(): LiveData<SignupResults> = signupResult

    /**
     * Called from activity on login button click
     */
    fun performSignup() {

        if (name.isBlank()) {
            signupResult.value = SignupResults.INVALID
            return
        }

        // TODO: ......

        signupResult.value = SignupResults.SUCCESS
    }

}
