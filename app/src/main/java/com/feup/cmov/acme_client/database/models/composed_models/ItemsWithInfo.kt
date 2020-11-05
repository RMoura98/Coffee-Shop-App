package com.feup.cmov.acme_client.database.models.composed_models

import androidx.room.Embedded
import androidx.room.Relation
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.OrderItem

data class ItemsWithInfo(
    @Embedded val orderItem: OrderItem,
    @Relation(
        parentColumn = "item_id",
        entityColumn = "id"
    )
    val menuItem: MenuItem
)