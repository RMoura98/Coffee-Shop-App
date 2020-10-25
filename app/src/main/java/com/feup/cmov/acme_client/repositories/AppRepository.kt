package com.feup.cmov.acme_client.repositories

import android.util.Log
import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.network.WebService
import com.feup.cmov.acme_client.network.requests.SignupRequest
import com.feup.cmov.acme_client.network.responses.SignupResponse
import javax.inject.Inject
import com.feup.cmov.acme_client.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class AppRepository
@Inject constructor(
    private val webService: WebService,
    private val appDatabaseDao: AppDatabaseDao
) {

    // Register user on the platform
    suspend fun performSignup(
        name: String,
        NIF: String,
        card_number: String,
        card_cvc: String,
        card_expiration: String,
        phone_number: String,
        userName: String,
        password: String
    ): Result<SignupResponse> {

        return withContext(Dispatchers.IO) {
            try {
                val request = SignupRequest(name=name, NIF=NIF, card_number=card_number, card_cvc=card_cvc, card_expiration=card_expiration, phone_number=phone_number)
                val response: SignupResponse = webService.createUser(request)

                val newUser = User(name=name, uuid=response.uuid, NIF=NIF, card_number=card_number, card_cvc=card_cvc, card_expiration=card_expiration, phone_number=phone_number, userName=userName)
                appDatabaseDao.createUser(newUser)

                Result.Success(response)
            }
            catch (e: Throwable) {
                when (e) {
                    is IOException -> Result.NetworkError
                    else -> Result.OtherError(e)
                }
            }
        }
    }

}
