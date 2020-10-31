package com.feup.cmov.acme_client.screens.vouchers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.repositories.VoucherRepository

class VouchersViewModel @ViewModelInject constructor(
    private val vouchersRepository: VoucherRepository
) : ViewModel() {

    private var unusedVouchers = vouchersRepository.getUnusedVouchers()
    fun getUnusedVouchers(): LiveData<List<Voucher>> = unusedVouchers

    fun refreshUnusedVouchers() {
        vouchersRepository.refreshUnusedVouchers()
    }
}