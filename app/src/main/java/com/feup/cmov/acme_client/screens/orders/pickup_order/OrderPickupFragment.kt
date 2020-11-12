package com.feup.cmov.acme_client.screens.orders.pickup_order

import android.graphics.*
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.MainActivity
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.comm.Packet
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.databinding.FragmentOrderPickupBinding
import com.feup.cmov.acme_client.utils.Measurements
import com.feup.cmov.acme_client.utils.ShowFeedback
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dagger.hilt.android.AndroidEntryPoint
import net.glxn.qrgen.android.QRCode
import java.nio.charset.Charset
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.database.models.Voucher
import com.google.gson.Gson


@AndroidEntryPoint
class OrderPickupFragment : Fragment(), NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private var hasNavigated: Boolean = false
    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(MainActivity.getActivity())
    lateinit var binding: FragmentOrderPickupBinding
    private val viewModel: OrderPickupViewModel by viewModels()
    lateinit var toTransmitString: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_pickup, container, false
        )

        val orderWithItems = OrderWithItems.deserialize(requireArguments().getString("order")!!)
        val packet = Packet(orderWithItems)
        toTransmitString = "${packet.signature},${packet.payloadString}"

        val qrCode: Bitmap =
            QRCode.from(toTransmitString).withSize(
                Measurements.convertDptoPx(360).toInt(),
                Measurements.convertDptoPx(360).toInt()
            ).withErrorCorrection(
                ErrorCorrectionLevel.Q
            ).bitmap()
        val merge = mergeBitmaps(
            BitmapFactory.decodeResource(resources, R.drawable.coffee_trimmed),
            qrCode
        )
        binding.qrCode.setImageBitmap(merge)

        binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener {
            activity?.onBackPressed();
        }

        viewModel.getCompleteOrder().observe(viewLifecycleOwner, Observer observe@{ orderWithItem ->
            if(orderWithItem != null && !hasNavigated) {
                var earnedVouchersJson = Gson().toJson(viewModel.getEarnedVouchers())
                container!!.findNavController().navigate(
                    R.id.action_orderPickupFragment_to_pickupSuccessFragment,
                    bundleOf(
                        "order" to OrderWithItems.serialize(orderWithItem),
                        "earnedVouchers" to earnedVouchersJson
                    )
                )
                hasNavigated = true
            }
        })

        viewModel.startRefresh(orderWithItems)

        if(nfcAdapter == null || !nfcAdapter.isEnabled) {
            // NFC Not supported
        }
        else {
            nfcAdapter!!.setNdefPushMessageCallback(this, MainActivity.getActivity())
            nfcAdapter!!.setOnNdefPushCompleteCallback(this, MainActivity.getActivity())
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        if(nfcAdapter != null) {
            nfcAdapter.setNdefPushMessageCallback(null, MainActivity.getActivity())
            nfcAdapter.setOnNdefPushCompleteCallback(null, MainActivity.getActivity())
        }
    }

    companion object {
        fun mergeBitmaps(logo: Bitmap?, qrcode: Bitmap): Bitmap? {
            val combined =
                Bitmap.createBitmap(qrcode.width, qrcode.height, qrcode.config)
            val canvas = Canvas(combined)
            val canvasWidth: Int = canvas.getWidth()
            val canvasHeight: Int = canvas.getHeight()
            canvas.drawBitmap(qrcode, Matrix(), null)
            val resizeLogo =
                Bitmap.createScaledBitmap(logo!!, canvasWidth / 6, canvasHeight / 6, true)
            val centreX = (canvasWidth - resizeLogo.width) / 2
            val centreY = (canvasHeight - resizeLogo.height) / 2
            val center = Matrix()
            center.setTranslate(centreX.toFloat(), centreY.toFloat())
            canvas.drawBitmap(toGrayscale(resizeLogo), center, null)
            return combined
        }

        fun toGrayscale(srcImage: Bitmap): Bitmap {
            val bmpGrayscale = Bitmap.createBitmap(
                srcImage.width,
                srcImage.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bmpGrayscale)
            val paint = Paint()
            val cm = ColorMatrix()
            cm.setSaturation(0f)
            paint.colorFilter = ColorMatrixColorFilter(cm)
            canvas.drawBitmap(srcImage, Matrix(), paint)
            return bmpGrayscale
        }

    }

    override fun createNdefMessage(event: NfcEvent?): NdefMessage {
        ShowFeedback.makeSnackbar("Message created")

        val ndefRecord = NdefRecord.createMime("application/com.feup.cmov.acme_client", toTransmitString.toByteArray())
        return NdefMessage(ndefRecord)
    }

    override fun onNdefPushComplete(event: NfcEvent?) {
        ShowFeedback.makeSnackbar("Message transmitted")
    }
}