package com.feup.cmov.acme_client.comm

import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.utils.PreferencesUtils
import com.feup.cmov.acme_client.utils.Security
import com.google.gson.Gson

class Packet (orderWithItems: OrderWithItems?) {
    lateinit var payload: Payload
    lateinit var payloadString: String
    lateinit var signature: String

    constructor(payload: Payload, payloadString: String, signature: String): this(null) {
        this.payload = payload
        this.payloadString = payloadString
        this.signature = signature
    }

    init {
        if(orderWithItems != null) {
            // fill payload
            payload = Payload(
                uuid = PreferencesUtils.getLoggedInUser().second!!,
                order_id = orderWithItems.order.order_id,
                orderItems = orderWithItems.orderItems.associateBy({it.orderItem.item_id}, {it.orderItem.quantity}),
                vouchers = orderWithItems.vouchers
            )
            payloadString = Gson().toJson(payload)
            // calculate signature
            val userName = PreferencesUtils.getLoggedInUser().first!!
            val key = Security.retrieveRsaPrivateKey(userName)
            val buffer = okio.Buffer()
            buffer.write(payloadString.toByteArray())
            signature = Security.makeSignature(key, buffer)
        }
    }

    companion object {
        fun serialize(packet: Packet): String {
            return "${packet.signature},${packet.payloadString}"
        }

        fun deserialize(packet: String): Packet {
            val (signature, payloadString) = packet.split(",".toRegex(), 2)
            val payload =  Gson().fromJson(payloadString, Payload::class.java)
            return Packet(payload, signature, signature)
        }
    }
}

data class Payload(val uuid: String, val order_id: String, val orderItems: Map<Long, Long>, val vouchers: List<Voucher>)