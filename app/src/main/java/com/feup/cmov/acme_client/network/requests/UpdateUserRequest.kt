package com.feup.cmov.acme_client.network.requests

data class UpdateUserRequest (
    val name: String?,
    val phone_number: String?,
    val NIF: String?,
    val card_number: String?,
    val card_expiration: String?,
    val card_cvc: String?
)