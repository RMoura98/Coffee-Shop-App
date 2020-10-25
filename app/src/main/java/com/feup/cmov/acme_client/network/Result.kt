package com.feup.cmov.acme_client.network

import org.json.JSONObject
import retrofit2.HttpException


sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    object NetworkError: Result<Nothing>()
    class OtherError(exception: Throwable) : Result<Nothing>() // TODO: implement server side errors displaying in client.
    {
        val msg: String

        init {
            if (exception is HttpException) {
                val body = JSONObject(exception.response()?.errorBody()?.string() ?: "{\"error\": \"Unknown Error.\"}")
                msg = body.getString("error")
            }
            else {
                msg = exception.message ?: "Unknown Error"
            }
        }
    }
}