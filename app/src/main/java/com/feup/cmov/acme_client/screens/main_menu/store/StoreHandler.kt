package com.feup.cmov.acme_client.screens.main_menu.store

import com.feup.cmov.acme_client.database.models.MenuItem

interface StoreHandler {
    fun addToCartOnClick(item: MenuItem)
}