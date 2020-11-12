package com.feup.cmov.acme_terminal.database.models

data class Voucher(
    val voucherId: String,
    val userId: String,
    val voucherType: String,
    val used_on_order_id: String?,
    val received_from_order_id: String
) {
    fun hasBeenUsed(): Boolean {
        return used_on_order_id != null
    }
}