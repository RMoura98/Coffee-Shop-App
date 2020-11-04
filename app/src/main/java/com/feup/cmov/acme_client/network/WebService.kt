package com.feup.cmov.acme_client.network

import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.network.requests.SignupRequest
import com.feup.cmov.acme_client.network.responses.LoginResponse
import com.feup.cmov.acme_client.network.responses.SignupResponse
import retrofit2.http.*


interface WebService {

    @POST("users/new")
    suspend fun createUser(@Body req: SignupRequest): SignupResponse

    @GET("users/{uuid}")
    suspend fun fetchUser(@Path("uuid") uuid: String): LoginResponse

    @AuthenticatedRequest
    @GET("menu")
    suspend fun fetchMenu(): List<MenuItem>

    @AuthenticatedRequest
    @GET("vouchers")
    suspend fun fetchUnusedVouchers(): List<Voucher>

    companion object {
        @Target(AnnotationTarget.FUNCTION)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class AuthenticatedRequest
    }
}