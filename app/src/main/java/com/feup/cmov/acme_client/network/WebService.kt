package com.feup.cmov.acme_client.network

import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.network.requests.PlaceOrderRequest
import com.feup.cmov.acme_client.network.requests.SignupRequest
import com.feup.cmov.acme_client.network.requests.UpdateUserRequest
import com.feup.cmov.acme_client.network.responses.*
import retrofit2.http.*


interface WebService {

    @POST("users/new")
    suspend fun createUser(@Body req: SignupRequest): SignupResponse

    @GET("users/{uuid}")
    suspend fun fetchUser(@Path("uuid") uuid: String): LoginResponse

    @PUT("users/{uuid}")
    suspend fun updateUser(@Path("uuid") uuid: String, @Body req: UpdateUserRequest): UpdateUserResponse

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
    suspend fun placeOrder(@Body req: PlaceOrderRequest): OrderStatusResponse

    @AuthenticatedRequest
    @GET("orders/status/{order_id}")
    suspend fun getOrderStatus(@Path("order_id") order_id: String): OrderStatusResponse

    companion object {
        @Target(AnnotationTarget.FUNCTION)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class AuthenticatedRequest
    }
}