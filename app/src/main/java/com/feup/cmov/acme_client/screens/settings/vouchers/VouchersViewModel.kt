package com.feup.cmov.acme_client.screens.settings.vouchers

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.database.models.composed_models.VoucherWithOrder
import com.feup.cmov.acme_client.repositories.VoucherRepository
import kotlinx.coroutines.launch

class VouchersViewModel @ViewModelInject constructor(
    private val vouchersRepository: VoucherRepository
) : ViewModel() {

    public var is_refreshing = ObservableField(false)

    private var vouchers = vouchersRepository.getAllVouchersWithOrders()
    fun getVouchers(): LiveData<List<VoucherWithOrder>> = vouchers

    fun refreshVouchers() {
        viewModelScope.launch{
            is_refreshing.set(true)
            vouchersRepository.refreshVouchers()
            is_refreshing.set(false)
        }
    }
}