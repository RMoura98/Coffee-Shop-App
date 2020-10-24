package com.feup.cmov.acme_client.repositories

import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.network.WebService
import com.feup.cmov.acme_client.network.requests.SignupRequest
import com.feup.cmov.acme_client.network.responses.SignupResponse
import java.util.concurrent.Executor
import javax.inject.Inject
import com.feup.cmov.acme_client.network.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception


class AppRepository
@Inject constructor(
    private val webService: WebService,
    private val userDao: AppDatabaseDao
) {

    // Register user on the platform
    suspend fun performSignup(
        fullName: String,
        NIF: String,
        userName: String,
        password: String
    ): Result<SignupResponse> {

        return withContext(Dispatchers.IO) {
            try {
                val request = SignupRequest(fullName = fullName, NIF = NIF)
                val response: SignupResponse = webService.createUser(request)

                val newUser = User(userName=userName, NIF=NIF)
                userDao.createUser(newUser)

                Result.Success(response)
            }
            catch (e: Throwable) {
                when (e) {
                    is IOException -> Result.NetworkError
                    is HttpException -> Result.HTTPError(e.code())
                    else -> Result.UnknownError
                }
            }
        }
    }

}
