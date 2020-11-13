package com.feup.cmov.acme_client.screens.settings.profile

import android.text.TextUtils
import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.forms.InvalidField
import com.feup.cmov.acme_client.network.Result
import com.feup.cmov.acme_client.repositories.UserRepository
import com.feup.cmov.acme_client.screens.settings.payment_method.PaymentMethodViewModel
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var user = userRepository.getLoggedInUser()
    fun getUser(): LiveData<User> = user

    private var updateUserResult = MutableLiveData<UpdateUserResult>()
    fun getUpdateUserResult(): LiveData<UpdateUserResult> = updateUserResult

    fun saveChanges() {
        val invalidFields = ArrayList<InvalidField>()

        validateInputFields(invalidFields)

        if (invalidFields.isNotEmpty()!!) {
            updateUserResult.postValue(UpdateUserResult.INVALID_FORM(invalidFields))
            return
        }

        viewModelScope.launch {
            val result: Result<Nothing?> = userRepository.updateUser(user.value!!)

            when (result) {
                is Result.Success -> updateUserResult.postValue(UpdateUserResult.SUCCESS)
                is Result.NetworkError -> updateUserResult.postValue(UpdateUserResult.NETWORK_ERROR)
                is Result.OtherError -> {
                    updateUserResult.postValue(UpdateUserResult.INVALID_FORM(invalidFields))
                    invalidFields.add(InvalidField(fieldName = "general", msg = result.msg))
                }
            }
        }
    }

    private fun validateInputFields(invalidFields: ArrayList<InvalidField>) {

        // >> Name
        if (user.value!!.name.isBlank())
            invalidFields.add(InvalidField(fieldName = "name", msg = "Insert name."))

        // >> Phone Number
        if (user.value!!.phone_number.isBlank())
            invalidFields.add(
                InvalidField(
                    fieldName = "phone_number",
                    msg = "Insert phone number."
                )
            )
        else if (!Patterns.PHONE.matcher(user.value!!.phone_number).matches())
            invalidFields.add(
                InvalidField(
                    fieldName = "phone_number",
                    msg = "Invalid phone number."
                )
            )
    }

    companion object {
        sealed class UpdateUserResult {
            data class INVALID_FORM(val invalidFields: List<InvalidField>) : UpdateUserResult()
            object NETWORK_ERROR : UpdateUserResult()
            object SUCCESS : UpdateUserResult()
        }
    }
}