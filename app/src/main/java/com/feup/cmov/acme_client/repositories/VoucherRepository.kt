package com.feup.cmov.acme_client.repositories

import androidx.lifecycle.LiveData
import com.feup.cmov.acme_client.utils.PreferencesUtils
import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.network.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VoucherRepository
@Inject constructor(
    private val webService: WebService,
    private val appDatabaseDao: AppDatabaseDao
) {

    fun getUnusedVouchers(): LiveData<List<Voucher>> {
        val (_, uuid) = PreferencesUtils.getLoggedInUser()

        val cached = uuid?.let { appDatabaseDao.getUnusedVouchers(it) }
        GlobalScope.launch {
            refreshUnusedVouchers()
        }

        return cached!!
    }

    suspend fun refreshUnusedVouchers() {
        withContext(Dispatchers.IO) {
            val (_, uuid) = PreferencesUtils.getLoggedInUser()
            val vouchers = uuid?.let { webService.fetchUnusedVouchers(it) };
            vouchers?.let { appDatabaseDao.deleteCreateVouchers(it) }
        }
    }
}