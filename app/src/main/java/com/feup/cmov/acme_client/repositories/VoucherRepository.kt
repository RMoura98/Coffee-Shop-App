package com.feup.cmov.acme_client.repositories

import androidx.lifecycle.LiveData
import com.feup.cmov.acme_client.utils.PreferencesUtils
import com.feup.cmov.acme_client.database.AppDatabaseDao
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.database.models.composed_models.VoucherWithOrder
import com.feup.cmov.acme_client.network.WebService
import com.feup.cmov.acme_client.utils.ShowFeedback
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

    fun getAllVouchers(): LiveData<List<Voucher>> {
        val (_, uuid) = PreferencesUtils.getLoggedInUser()

        val cached = appDatabaseDao.getAllVouchers(uuid!!)
        GlobalScope.launch {
            refreshVouchers()
        }

        return cached
    }

    fun getAllVouchersWithOrders(): LiveData<List<VoucherWithOrder>> {
        val (_, uuid) = PreferencesUtils.getLoggedInUser()

        val cached = appDatabaseDao.getAllVouchersWithOrder(uuid!!)
        GlobalScope.launch {
            refreshVouchers()
        }

        return cached
    }

    suspend fun refreshVouchers() {
        withContext(Dispatchers.IO) {
            try {
                val vouchers = webService.fetchVouchers()
                appDatabaseDao.createVouchers(vouchers)
            } catch (e: Exception) {
                ShowFeedback.makeSnackbar(e.toString())
            }
        }
    }
}