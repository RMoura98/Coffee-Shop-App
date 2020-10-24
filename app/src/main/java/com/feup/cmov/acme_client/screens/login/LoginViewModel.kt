package com.feup.cmov.acme_client.screens.login;

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.feup.cmov.acme_client.repositories.AppRepository
import com.feup.cmov.acme_client.services.WebService

class LoginViewModel @ViewModelInject constructor(appRepository: AppRepository, webService: WebService) : ViewModel() {

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
