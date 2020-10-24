package com.feup.cmov.acme_client.network

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class HTTPError(val code: Int): Result<Nothing>()
    object NetworkError: Result<Nothing>()
    object UnknownError: Result<Nothing>()
}