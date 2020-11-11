package com.feup.cmov.acme_terminal.network

import com.feup.cmov.acme_terminal.network.requests.PlaceOrderRequest
import com.feup.cmov.acme_terminal.network.responses.PlaceOrderResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface WebService {

    @POST("orders")
    suspend fun placeOrder(@Body req: PlaceOrderRequest, @Header("Authorization") signature: String): PlaceOrderResponse
}