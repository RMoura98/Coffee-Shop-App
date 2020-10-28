package com.feup.cmov.acme_client.screens.main_menu.store

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.repositories.UserRepository
import com.feup.cmov.acme_client.repositories.MenuRepository
import com.feup.cmov.acme_client.screens.login.LoginViewModel
import kotlinx.coroutines.launch

class StoreViewModel @ViewModelInject constructor(
    menuRepository: MenuRepository,
    userRepository: UserRepository
) : ViewModel() {


    private var menuItems = menuRepository.getMenu()
    fun getMenuItems(): LiveData<List<MenuItem>> = menuItems

}
