package com.feup.cmov.acme_client.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.feup.cmov.acme_client.utils.PreferencesUtils
import com.feup.cmov.acme_client.utils.Security
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

/**
 * Repository which houses methods related to Users.
 */
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

                PreferencesUtils.updateLoggedInUser(userName, response.uuid)
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

                PreferencesUtils.updateLoggedInUser(userName, uuid)
                Result.Success(user)
            } catch (e: Throwable) {
                when (e) {
                    is IOException -> Result.NetworkError
                    else -> Result.OtherError(e)
                }
            }
        }
    }

    fun getLoggedInUser(): LiveData<User> {
        val (userName, uuid) = PreferencesUtils.getLoggedInUser()

        if(userName == null || uuid == null)
            return MutableLiveData<User>(null)
        else
            return appDatabaseDao.loadUserAsync(userName)
    }
}
