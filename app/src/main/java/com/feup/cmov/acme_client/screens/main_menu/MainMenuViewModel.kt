package com.feup.cmov.acme_client.screens.main_menu

import androidx.databinding.ObservableField
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.repositories.AppRepository

class MainMenuViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    appRepository: AppRepository
) : ViewModel() {

    val userName: String =
        savedStateHandle["userName"] ?: throw IllegalArgumentException("missing username")
    val user: LiveData<User> = appRepository.fetchUser(userName)

}
