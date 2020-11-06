package com.feup.cmov.acme_client.screens.main_menu.voucher_selection

import com.feup.cmov.acme_client.database.models.Voucher

interface VoucherSelectionHandler {
    fun onCheckboxTick(voucher: Voucher, checked: Boolean)
}