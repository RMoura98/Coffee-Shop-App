package com.feup.cmov.acme_client.screens.login;

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.forms.InvalidField
import com.feup.cmov.acme_client.network.Result
import com.feup.cmov.acme_client.repositories.AppRepository
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(val appRepository: AppRepository) : ViewModel() {

    /**
     * Two way bind-able fields
     */
    var userName: String = ""
    var password: String = ""

    /**
     * This variable is bound to the progress bar.
     */
    var isLoading = ObservableField<Boolean>(false)

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
            isLoading.set(true)
            val result: Result<User> = appRepository.performLogin(userName, password)

            when (result) {
                is Result.Success -> loginResult.postValue(LoginResults.SUCCESS(result.data))
                is Result.NetworkError -> loginResult.postValue(LoginResults.NETWORK_ERROR)
                is Result.OtherError -> {
                    loginResult.postValue(LoginResults.INVALID_FORM(invalidFields))
                    invalidFields.add(InvalidField(fieldName="general", msg=result.msg))
                }
            }
            isLoading.set(false)
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

        @BindingAdapter("android:visibility")
        @JvmStatic
        fun setVisibility(view: View, isLoading: Boolean) {
            view.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

}
