package com.feup.cmov.acme_client.screens.checkout.voucher_selection

import com.feup.cmov.acme_client.database.models.Voucher

interface VoucherSelectionHandler {
    fun onCheckboxTick(voucher: Voucher, checked: Boolean)
}