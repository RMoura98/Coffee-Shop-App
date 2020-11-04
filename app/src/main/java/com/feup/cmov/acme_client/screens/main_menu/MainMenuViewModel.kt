package com.feup.cmov.acme_client.screens.main_menu

import androidx.fragment.app.Fragment
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.repositories.MenuRepository
import com.feup.cmov.acme_client.repositories.UserRepository
import com.feup.cmov.acme_client.screens.main_menu.store.StoreFragment
import com.feup.cmov.acme_client.screens.orders.OrdersHistoryFragment
import com.feup.cmov.acme_client.screens.settings.SettingsFragment
import com.feup.cmov.acme_client.screens.settings.vouchers.VouchersFragment

class MainMenuViewModel: ViewModel() {

    private val navFragments = mapOf(
        R.id.storeAction to StoreFragment(),
        R.id.historyAction to OrdersHistoryFragment(),
        R.id.settingsAction to SettingsFragment()
    )

    private var currentAction = R.id.storeAction

    fun getCurrentAction(): Int {
        return currentAction
    }

    fun getCurrentFragment(): Fragment {
        return navFragments.getValue(currentAction)
    }

    fun setCurrentAction(action: Int) {
        currentAction = action
    }
}
