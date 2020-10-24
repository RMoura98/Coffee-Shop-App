package com.feup.cmov.acme_client.login

import android.view.View

interface LoginHandler {
    fun onLoginButtonClick(v: View)
    fun onSignupButtonClick(v: View)
}