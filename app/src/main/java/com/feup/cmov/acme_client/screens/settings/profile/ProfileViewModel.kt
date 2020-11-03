package com.feup.cmov.acme_client.screens.settings.profile

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.repositories.UserRepository

class ProfileViewModel @ViewModelInject constructor(
    userRepository: UserRepository
) : ViewModel() {

    private var user = userRepository.getLoggedInUser()

    fun getUser(): LiveData<User> = user

}