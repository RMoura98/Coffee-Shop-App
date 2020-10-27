package com.feup.cmov.acme_client.repositories

import androidx.lifecycle.LiveData
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.Utils.Security
import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.network.WebService
import com.feup.cmov.acme_client.network.requests.SignupRequest
import com.feup.cmov.acme_client.network.responses.SignupResponse
import javax.inject.Inject
import com.feup.cmov.acme_client.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception


class UserRepository
@Inject constructor(
    private val webService: WebService,
    private val appDatabaseDao: AppDatabaseDao
) {

    // Register user on the platform.
    suspend fun performSignup(
        name: String,
        NIF: String,
        card_number: String,
        card_cvc: String,
        card_expiration: String,
        phone_number: String,
        userName: String,
        password: String
    ): Result<User> {

        return withContext(Dispatchers.IO) {
            try {
                // Create RSA key pair.
                val keyPair = Security.generateRsaKeyPair(userName)

                // Create Request
                val request = SignupRequest(
                    name = name,
                    NIF = NIF,
                    card_number = card_number,
                    card_cvc = card_cvc,
                    card_expiration = card_expiration,
                    phone_number = phone_number,
                    public_key = Security.getRSAKeyAsString(keyPair.public)
                )
                // Send HTTP request.
                val response: SignupResponse = webService.createUser(request)
                // Insert into local database.
                val newUser = User(
                    name = name,
                    password_hashed = Security.generateHashedPassword(password),
                    uuid = response.uuid,
                    NIF = NIF,
                    card_number = card_number,
                    card_cvc = card_cvc,
                    card_expiration = card_expiration,
                    phone_number = phone_number,
                    userName = userName
                )
                appDatabaseDao.createUser(newUser)

                val preferences = AcmeApplication.getPreferences()
                with(preferences.edit()) {
                    putString(
                        AcmeApplication.getAppContext().getString(R.string.preferences_userName),
                        userName
                    )
                    apply()
                }

                // Return success.
                Result.Success(newUser)
            } catch (e: Throwable) {
                when (e) {
                    is IOException -> Result.NetworkError
                    else -> Result.OtherError(e)
                }
            }
        }
    }

    // Authenticates the user in the platform.
    suspend fun performLogin(
        userName: String,
        password: String
    ): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val user =
                    appDatabaseDao.loadUser(userName) ?: throw Exception("User does not exist.")

                if (!Security.isPasswordCorrect(password, user.password_hashed))
                    throw Exception("Password is not correct.")

                val uuid = user.uuid
                webService.fetchUser(uuid)

                val preferences = AcmeApplication.getPreferences()
                with(preferences.edit()) {
                    putString(
                        AcmeApplication.getAppContext().getString(R.string.preferences_userName),
                        userName
                    )
                    apply()
                }
                System.out.println("Saved user")

                Result.Success(user)
            } catch (e: Throwable) {
                when (e) {
                    is IOException -> Result.NetworkError
                    else -> Result.OtherError(e)
                }
            }
        }
    }

    suspend fun getLoggedInUser(): User? {
        return withContext(Dispatchers.IO) {
            val preferences = AcmeApplication.getPreferences()
            val userName = preferences.getString(
                AcmeApplication.getAppContext().getString(R.string.preferences_userName), null
            )
            if(userName == null)
                null
            else
                appDatabaseDao.loadUser(userName)
        }
    }

    fun getLoggedInUserLiveData(): LiveData<User> {
        val preferences = AcmeApplication.getPreferences()
        val userName = preferences.getString(
            AcmeApplication.getAppContext().getString(R.string.preferences_userName), null
        )
        if(userName == null)
            throw Exception("User is not logged in.")
        else
            return appDatabaseDao.loadUserAsync(userName)
    }
}
