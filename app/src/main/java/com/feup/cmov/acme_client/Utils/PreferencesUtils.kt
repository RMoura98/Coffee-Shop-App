package com.feup.cmov.acme_client.Utils

import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R

object PreferencesUtils {
    /**
     * Updates the local storage of the application to reflect the newly logged in user.
     */
    fun updateLoggedInUser(userName: String, uuid: String) {
        val preferences = AcmeApplication.getPreferences()
        with(preferences.edit()) {
            putString(
                AcmeApplication.getAppContext().getString(R.string.preferences_userName),
                userName
            )
            putString(
                AcmeApplication.getAppContext().getString(R.string.preferences_userUUID),
                uuid
            )
            apply()
        }
    }

    /**
     * Returns a Pair containing the <userName, uuid> of the current logged in user.
     * If there is no logged in user: userName and uuid will be null.
     */
    fun getLoggedInUser(): Pair<String?,String?> {
        val preferences = AcmeApplication.getPreferences()
        val userName = preferences.getString(
            AcmeApplication.getAppContext().getString(R.string.preferences_userName), null
        )
        val uuid = preferences.getString(
            AcmeApplication.getAppContext().getString(R.string.preferences_userUUID), null
        )
        return Pair(userName, uuid)
    }
}