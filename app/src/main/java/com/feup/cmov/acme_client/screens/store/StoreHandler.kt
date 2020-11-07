package com.feup.cmov.acme_client.screens.store

import android.graphics.drawable.Drawable
import com.feup.cmov.acme_client.database.models.MenuItem

interface StoreHandler {
    fun addToCartOnClick(item: MenuItem, x: Float, y: Float, drawable: Drawable)
}