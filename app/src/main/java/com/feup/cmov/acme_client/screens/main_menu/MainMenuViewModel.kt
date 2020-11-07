package com.feup.cmov.acme_client.screens.main_menu

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.screens.store.StoreFragment
import com.feup.cmov.acme_client.screens.orders.OrdersHistoryFragment
import com.feup.cmov.acme_client.screens.settings.SettingsFragment

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
