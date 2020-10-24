package com.feup.cmov.acme_client.login;

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R

class LoginViewModel : ViewModel() {

    /**
     * Two way bind-able fields
     */
    var userName: String = ""
    var password: String = ""

    /**
     * To pass login result to activity
     */

    enum class LoginResults {
        INVALID_USERNAME, INVALID_PASSWORD, SUCCESS
    }

    private val loginResult = MutableLiveData<LoginResults>()

    fun getLoginResult(): LiveData<LoginResults> = loginResult

    /**
     * Called from activity on login button click
     */
    fun performLogin() {

        if (userName.isBlank()) {
            loginResult.value = LoginResults.INVALID_USERNAME
            return
        }

        if (password.isBlank()) {
            loginResult.value = LoginResults.INVALID_PASSWORD
            return
        }

        loginResult.value = LoginResults.SUCCESS
    }

}
