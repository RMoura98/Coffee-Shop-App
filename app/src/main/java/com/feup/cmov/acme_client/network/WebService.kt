package com.feup.cmov.acme_client.network

import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.network.requests.PlaceOrderRequest
import com.feup.cmov.acme_client.network.requests.SignupRequest
import com.feup.cmov.acme_client.network.responses.FetchOrdersResponse
import com.feup.cmov.acme_client.network.responses.LoginResponse
import com.feup.cmov.acme_client.network.responses.PlaceOrderResponse
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
    suspend fun fetchVouchers(): List<Voucher>

    @AuthenticatedRequest
    @GET("orders")
    suspend fun fetchOrders(): FetchOrdersResponse

    @AuthenticatedRequest
    @POST("orders")
    suspend fun placeOrder(@Body req: PlaceOrderRequest): PlaceOrderResponse

    companion object {
        @Target(AnnotationTarget.FUNCTION)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class AuthenticatedRequest
    }
}