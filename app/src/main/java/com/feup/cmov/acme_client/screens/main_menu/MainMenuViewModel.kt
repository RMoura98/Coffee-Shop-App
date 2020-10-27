package com.feup.cmov.acme_client.screens.main_menu

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.repositories.UserRepository
import com.feup.cmov.acme_client.repositories.MenuRepository
import com.feup.cmov.acme_client.screens.login.LoginViewModel
import kotlinx.coroutines.launch

class MainMenuViewModel @ViewModelInject constructor(
    menuRepository: MenuRepository,
    userRepository: UserRepository
) : ViewModel() {


    private var menuItems = menuRepository.getMenu()
    fun getMenuItems(): LiveData<List<MenuItem>> = menuItems

    var user: User? = null
    init {
        // Redirect to Main Menu if user is logged in.
        viewModelScope.launch {
            val user = userRepository.getLoggedInUser()
        }
    }

}
