package com.feup.cmov.acme_client.screens.main_menu

import androidx.fragment.app.Fragment
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.repositories.MenuRepository
import com.feup.cmov.acme_client.repositories.UserRepository
import com.feup.cmov.acme_client.screens.main_menu.store.StoreFragment
import com.feup.cmov.acme_client.screens.settings.SettingsFragment
import com.feup.cmov.acme_client.screens.settings.vouchers.VouchersFragment

class MainMenuViewModel @ViewModelInject constructor(
    menuRepository: MenuRepository
): ViewModel() {

    private var menuItems = menuRepository.getMenu()
    fun getMenuItems(): LiveData<List<MenuItem>> = menuItems

    private val navFragments = mapOf(
        R.id.storeAction to StoreFragment::class.java,
        R.id.vouchersAction to VouchersFragment::class.java,
        R.id.cartAction to StoreFragment::class.java,
        R.id.historyAction to StoreFragment::class.java,
        R.id.settingsAction to SettingsFragment::class.java
    )

    private var currentAction = R.id.storeAction

    fun getCurrentAction(): Int {
        return currentAction
    }

    fun getCurrentFragment(): Fragment {
        return navFragments.getValue(currentAction).newInstance()
    }

    fun setCurrentAction(action: Int) {
        currentAction = action
    }
}
