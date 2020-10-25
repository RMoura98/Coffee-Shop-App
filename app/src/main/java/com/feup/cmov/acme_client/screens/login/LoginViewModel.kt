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

        if (userName.isBlank())
            invalidFields.value?.add(InvalidField(fieldName="userName", msg="Insert username."))

        if (password.isBlank())
            invalidFields.value?.add(InvalidField(fieldName="password", msg="Insert password."))

        if (invalidFields.value?.isNotEmpty()!!) {
            loginResult.value = LoginResults.INVALID_FORM
            return
        }
    }

}
