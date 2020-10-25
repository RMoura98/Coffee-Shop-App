package com.feup.cmov.acme_client.screens.login;

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    /**
     * To pass login result to activity
     */
    enum class LoginResults {
        INVALID_FORM, NETWORK_ERROR, SUCCESS
    }

    private var invalidFields = MutableLiveData<ArrayList<InvalidField>>()
    private val loginResult = MutableLiveData<LoginResults>()

    fun getLoginResult(): LiveData<LoginResults> = loginResult
    fun getInvalidFields(): LiveData<ArrayList<InvalidField>> = invalidFields

    /**
     * Called from activity on login button click
     */
    fun performLogin() {

        // Empty all errors.
        invalidFields.value = ArrayList()

        when {
            userName.isBlank() -> invalidFields.value?.add(InvalidField(fieldName="userName", msg="Insert username."))
            password.isBlank() -> invalidFields.value?.add(InvalidField(fieldName="password", msg="Insert password."))
        }

        if (invalidFields.value?.isNotEmpty()!!) {
            loginResult.value = LoginResults.INVALID_FORM
            return
        }


        viewModelScope.launch {
            val result: Result<LoginResponse> = appRepository.performLogin(userName, password)

            when (result) {
                is Result.Success -> loginResult.postValue(LoginResults.SUCCESS)
                is Result.NetworkError -> loginResult.postValue(LoginResults.NETWORK_ERROR)
                is Result.OtherError -> {
                    loginResult.postValue(LoginResults.INVALID_FORM)
                    invalidFields.value?.add(InvalidField(fieldName="general", msg=result.msg))
                }
            }
        }

    }

}
