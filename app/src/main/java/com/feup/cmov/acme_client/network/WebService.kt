package com.feup.cmov.acme_client.network

import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.network.requests.SignupRequest
import com.feup.cmov.acme_client.network.responses.SignupResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface WebService {

    @POST("user/new")
    suspend fun createUser(@Body req: SignupRequest): SignupResponse

}