package com.feup.cmov.acme_client.screens.login;

import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.network.requests.SignupRequest
import com.feup.cmov.acme_client.network.responses.SignupResponse
import com.feup.cmov.acme_client.repositories.AppRepository
import kotlinx.coroutines.launch
import com.feup.cmov.acme_client.network.Result

class LoginViewModel @ViewModelInject constructor(val appRepository: AppRepository) : ViewModel() {

    /**
     * Two way bind-able fields
     */
    var userName: String = ""
    var password: String = ""

    /**
     * To pass login result to activity
     */
    enum class LoginResults {
        INVALID_USERNAME, INVALID_PASSWORD, NETWORK_ERROR, SUCCESS
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

        viewModelScope.launch {
            val result: Result<SignupResponse> = appRepository.performSignup(userName=userName, password=password, fullName="", NIF="")

            when (result) {
                is Result.Success -> loginResult.value = LoginResults.SUCCESS
                else -> loginResult.value = LoginResults.NETWORK_ERROR
            }
        }
    }

}
