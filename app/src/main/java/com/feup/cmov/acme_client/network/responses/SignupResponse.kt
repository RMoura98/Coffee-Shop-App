package com.feup.cmov.acme_client.network.responses

import com.google.gson.annotations.SerializedName

class SignupResponse {
    @SerializedName("uuid")
    lateinit var uuid: String
}