package com.feup.cmov.acme_client.screens.main_menu

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.screens.main_menu.store.StoreFragment
import com.feup.cmov.acme_client.screens.settings.SettingsFragment
import com.feup.cmov.acme_client.screens.settings.vouchers.VouchersFragment

class MainMenuViewModel: ViewModel() {

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
