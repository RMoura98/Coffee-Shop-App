package com.feup.cmov.acme_client.network

import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.network.requests.SignupRequest
import com.feup.cmov.acme_client.network.responses.LoginResponse
import com.feup.cmov.acme_client.network.responses.SignupResponse
import retrofit2.http.*


interface WebService {

    @POST("user/new")
    suspend fun createUser(@Body req: SignupRequest): SignupResponse

    @AuthenticatedRequest
    @GET("user/{uuid}")
    suspend fun fetchUser(@Path("uuid") uuid: String): LoginResponse

    @AuthenticatedRequest
    @GET("menu")
    suspend fun fetchMenu(): List<MenuItem>

    companion object {
        @Target(AnnotationTarget.FUNCTION)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class AuthenticatedRequest
    }
}