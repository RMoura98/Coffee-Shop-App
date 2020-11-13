package com.feup.cmov.acme_client.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.google.gson.Gson

@Entity(
    tableName = "voucher_table"
)
data class Voucher(
    @PrimaryKey
    val voucherId: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "voucher_type")
    val voucherType: String,

    @ColumnInfo(name = "used_on_order_id")
    val used_on_order_id: String?,

    @ColumnInfo(name = "received_from_order_id")
    val received_from_order_id: String
) {
    fun hasBeenUsed(): Boolean {
        return used_on_order_id != null
    }

    companion object {
        fun serialize(voucher: Voucher): String {
            return Gson().toJson(voucher)
        }

        fun deserialize(voucher: String): Voucher {
            return Gson().fromJson(voucher, Voucher::class.java)
        }
    }
}