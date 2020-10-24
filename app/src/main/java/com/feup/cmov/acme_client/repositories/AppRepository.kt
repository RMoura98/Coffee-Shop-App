package com.feup.cmov.acme_client.repositories

import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.services.WebService
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AppRepository// Simple in-memory cache. Details omitted for brevity.
@Inject constructor(
    private val webService: WebService,
    private val executor: Executor,
    private val userDao: AppDatabaseDao
) {

    fun getUser(userId: String) { // LiveData<User>
        refreshUser(userId)
        // Returns a LiveData object directly from the database.
        //return userDao.load(userId)
    }

    private fun refreshUser(userId: String) {
        // Runs in a background thread.
        executor.execute {
            // Check if user data was fetched recently.
            //val userExists = userDao.hasUser(FRESH_TIMEOUT)
            //if (!userExists) {
                // Refreshes the data.
            //    val response = webservice.getUser(userId).execute()

                // Check for errors here.

                // Updates the database. The LiveData object automatically
                // refreshes, so we don't need to do anything else here.
             //   userDao.save(response.body()!!)
            //}
        }
    }

    companion object {
        val FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1)
    }
}
