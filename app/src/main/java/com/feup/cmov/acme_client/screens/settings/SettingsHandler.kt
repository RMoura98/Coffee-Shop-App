package com.feup.cmov.acme_client.screens.settings

import android.view.View

interface SettingsHandler {
    fun onProfileButtonClick(v: View)
    fun onPaymentMethodButtonClick(v: View)
    fun onVouchersButtonClick(v: View)
    fun onLogoutButtonClick(v: View)
}