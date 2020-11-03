package com.feup.cmov.acme_client.utils

import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Debug @Inject constructor(
    private val appDatabaseDao: AppDatabaseDao
) {
    fun startDebugMode() {
        isDebugEnabled = true;
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                seed()
            }
        }
    }

    private fun seed() {
        if(appDatabaseDao.loadUser("panda") == null) {
            val user = User(
                1000,
                "95850c47-bfa2-4254-84a8-36b587dfeb27",
                "Fabio Oliveira",
                "\$2a\$10\$MCjNS3rC2QJ11IK89nXF8evJ02Aurfeti6oI0oW1KP6.WC2WENYMi",
                "panda",
                "249720955",
                "450077003964930",
                "748",
                "12/24",
                "931798250"
            )
            Security.generateRsaKeyPair(user.userName)
            appDatabaseDao.createUser(user)
        }
    }

    companion object {
        var isDebugEnabled = false
    }
}