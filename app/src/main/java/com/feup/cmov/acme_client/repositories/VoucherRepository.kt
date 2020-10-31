package com.feup.cmov.acme_client.repositories

import androidx.lifecycle.LiveData
import com.feup.cmov.acme_client.Utils.PreferencesUtils
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
        uuid?.let { refreshUnusedVouchers(it) }

        return cached!!
    }

    fun refreshUnusedVouchers(userId: String) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val vouchers = webService.fetchUnusedVouchers(userId);
                appDatabaseDao.createVoucher(vouchers);
            }
        }
    }
}