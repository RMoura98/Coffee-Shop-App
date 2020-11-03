package com.feup.cmov.acme_client.screens.main_menu.cart

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.repositories.UserRepository
import com.feup.cmov.acme_client.repositories.MenuRepository
import com.feup.cmov.acme_client.screens.login.LoginViewModel
import kotlinx.coroutines.launch

class CartViewModel @ViewModelInject constructor(
    menuRepository: MenuRepository,
    userRepository: UserRepository
) : ViewModel() {

}
