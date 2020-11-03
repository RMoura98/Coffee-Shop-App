package com.feup.cmov.acme_client.screens.settings.vouchers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.repositories.VoucherRepository
import kotlinx.coroutines.launch

class VouchersViewModel @ViewModelInject constructor(
    private val vouchersRepository: VoucherRepository
) : ViewModel() {

    private var unusedVouchers = vouchersRepository.getUnusedVouchers()
    fun getUnusedVouchers(): LiveData<List<Voucher>> = unusedVouchers

    fun refreshUnusedVouchers() {
        viewModelScope.launch{
            vouchersRepository.refreshUnusedVouchers()
        }
    }
}