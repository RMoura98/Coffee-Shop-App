package com.feup.cmov.acme_client.services

import com.feup.cmov.acme_client.database.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WebService {
    /**
     * @GET declares an HTTP GET request
     * @Path("user") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("/users/{user}")
    fun getUser(@Path("user") userId: String): Call<User>
}
