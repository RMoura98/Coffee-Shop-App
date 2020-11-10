package com.feup.cmov.acme_client

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.fragment.app.FragmentManager
import com.feup.cmov.acme_client.databinding.ActivityMainBinding
import com.feup.cmov.acme_client.screens.checkout.order_placed.OrderPlacedFragment
import com.feup.cmov.acme_client.screens.orders.pickup_success.PickupSuccessFragment
import com.feup.cmov.acme_client.utils.Debug
import com.feup.cmov.acme_client.utils.ShowFeedback
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // TODO: REMOVE THIS BEFORE TURNING IN PROJECT!
    @Inject
    lateinit var debug: Debug
    private var nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(AcmeApplication.getAppContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        debug.startDebugMode()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        activity = this
        Companion.fragmentManager = supportFragmentManager
    }

    override fun onBackPressed() {
        // Prevent pressing back button on some fragments.
        // Namely: OrderPlacedFragment
        try {
            val navHostFragment: NavHostFragment =
                supportFragmentManager.findFragmentById(R.id.acmeNavHostFragment) as NavHostFragment
            navHostFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                if (fragment !is OrderPlacedFragment && fragment !is PickupSuccessFragment)
                    super.onBackPressed()
            }
        }
        catch(e: Exception) {
            Log.e("MainActivity", e.toString())
        }
    }

    override fun onResume() {
        super.onResume()

        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        enableForegroundDispatch(this, this.nfcAdapter)
        receiveMessageFromDevice(intent)
    }

    override fun onPause() {
        super.onPause()
        disableForegroundDispatch(this, this.nfcAdapter)
    }

    override fun onNewIntent(intent: Intent) {
        receiveMessageFromDevice(intent)
        super.onNewIntent(intent)
    }

    private fun receiveMessageFromDevice(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)!!
            with(parcelables) {
                val inNdefMessage = this[0] as NdefMessage
                val inNdefRecords = inNdefMessage.records
                val ndefRecord_0 = inNdefRecords[0]

                val inMessage = String(ndefRecord_0.payload)
                ShowFeedback.makeSnackbar(inMessage)
            }
        }
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)!!
            with(parcelables) {
                val inNdefMessage = this[0] as NdefMessage
                val inNdefRecords = inNdefMessage.records
                val ndefRecord_0 = inNdefRecords[0]

                val inMessage = String(ndefRecord_0.payload)
                ShowFeedback.makeSnackbar(inMessage)
            }
        }
    }

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {

        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters

        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType("text/plain")
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("Check your MIME type")
            }
        }

        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    private fun disableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        adapter?.disableForegroundDispatch(activity)
    }

    companion object {
        private lateinit var activity: Activity
        private lateinit var fragmentManager: FragmentManager

        fun getActivity(): Activity {
            return activity
        }

        fun getFragmentManager(): FragmentManager {
            return fragmentManager
        }
    }
}