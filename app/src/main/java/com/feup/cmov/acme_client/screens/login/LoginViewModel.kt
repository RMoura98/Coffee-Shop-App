package com.feup.cmov.acme_client.screens.login;

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.forms.InvalidField
import com.feup.cmov.acme_client.network.responses.SignupResponse
import com.feup.cmov.acme_client.repositories.AppRepository
import kotlinx.coroutines.launch
import com.feup.cmov.acme_client.network.Result
import com.feup.cmov.acme_client.network.responses.LoginResponse
import com.feup.cmov.acme_client.screens.signup.SignupViewModel

class LoginViewModel @ViewModelInject constructor(val appRepository: AppRepository) : ViewModel() {

    /**
     * Two way bind-able fields
     */
    var userName: String = ""
    var password: String = ""

    private val loginResult = MutableLiveData<LoginResults>()

    fun getLoginResult(): LiveData<LoginResults> = loginResult

    /**
     * Called from activity on login button click
     */
    fun performLogin() {

        val invalidFields = ArrayList<InvalidField>()

        when {
            userName.isBlank() -> invalidFields.add(InvalidField(fieldName="userName", msg="Insert username."))
            password.isBlank() -> invalidFields.add(InvalidField(fieldName="password", msg="Insert password."))
        }

        if (invalidFields.isNotEmpty()) {
            loginResult.postValue(LoginResults.INVALID_FORM(invalidFields))
            return
        }


        viewModelScope.launch {
            val result: Result<User> = appRepository.performLogin(userName, password)

            when (result) {
                is Result.Success -> loginResult.postValue(LoginResults.SUCCESS(result.data))
                is Result.NetworkError -> loginResult.postValue(LoginResults.NETWORK_ERROR)
                is Result.OtherError -> {
                    loginResult.postValue(LoginResults.INVALID_FORM(invalidFields))
                    invalidFields.add(InvalidField(fieldName="general", msg=result.msg))
                }
            }
        }

    }

    companion object {
        /**
         * To pass login result to fragment
         */
        sealed class LoginResults {
            data class INVALID_FORM(val invalidFields: List<InvalidField>): LoginResults()
            object NETWORK_ERROR: LoginResults()
            data class SUCCESS(val user: User) : LoginResults()
        }
    }

}
