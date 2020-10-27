package com.feup.cmov.acme_client.repositories

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.network.WebService
import kotlinx.coroutines.*
import retrofit2.await
import javax.inject.Inject
import java.util.concurrent.Executor


class MenuRepository
@Inject constructor(
    private val webService: WebService,
    private val appDatabaseDao: AppDatabaseDao
) {

    // Retrieves the last menu from the server.
    fun getMenu(): LiveData<List<MenuItem>> {
        val cached = appDatabaseDao.getMenu()
        refreshMenu()
        return cached
    }

    // Updates local database with the menu from the API.
    private fun refreshMenu() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val menuItems = webService.fetchMenu()
                    appDatabaseDao.createMenuItem(menuItems)
                } catch (e: Throwable) {
                    Log.e("MenuRepository", "refreshMenu: $e")
                }
            }
        }
    }
}

