package com.feup.cmov.acme_client.screens.settings.profile

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.repositories.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var user = userRepository.getLoggedInUser()

    fun getUser(): LiveData<User> = user

    fun saveChanges() {
        viewModelScope.launch {
            userRepository.updateUser(user.value!!)
        }
    }
}