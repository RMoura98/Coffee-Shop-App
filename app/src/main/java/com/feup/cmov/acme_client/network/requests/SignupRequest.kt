package com.feup.cmov.acme_client.network.requests;

data class SignupRequest(val name: String, val NIF: String, val card_number: String, val card_cvc: String, val card_expiration: String, val phone_number: String) {

}