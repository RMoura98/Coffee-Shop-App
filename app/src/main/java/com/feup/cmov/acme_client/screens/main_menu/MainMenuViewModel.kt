package com.feup.cmov.acme_client.screens.main_menu

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.repositories.MenuRepository

class MainMenuViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    menuRepository: MenuRepository
) : ViewModel() {

    val userName: String =
        savedStateHandle["userName"] ?: throw IllegalArgumentException("missing username")
    private var menuItems = menuRepository.getMenu()

    fun getMenuItems(): LiveData<List<MenuItem>> = menuItems

}
