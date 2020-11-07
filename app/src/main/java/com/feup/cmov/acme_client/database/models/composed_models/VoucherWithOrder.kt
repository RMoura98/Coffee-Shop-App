package com.feup.cmov.acme_client.database.models.composed_models

import androidx.room.Embedded
import androidx.room.Relation
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.database.models.OrderItem
import com.feup.cmov.acme_client.database.models.Voucher

data class VoucherWithOrder(
    @Embedded val voucher: Voucher,
    @Relation(
        parentColumn = "used_on_order_id",
        entityColumn = "order_id"
    )
    val order: Order?
)