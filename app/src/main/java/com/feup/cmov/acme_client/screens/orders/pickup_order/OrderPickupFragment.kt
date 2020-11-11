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


@AndroidEntryPoint
class OrderPickupFragment : Fragment(), NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(MainActivity.getActivity())
    lateinit var binding: FragmentOrderPickupBinding
    private val viewModel: OrderPickupViewModel by viewModels()

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

        Log.e("here", "${packet.signature},${packet.payloadString}".length.toString())

        val qrCode: Bitmap =
            QRCode.from("${packet.signature},${packet.payloadString}").withSize(
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

        viewModel.startRefresh(orderWithItems.order)
        viewModel.isOrderComplete().addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(obs: Observable?, propertyId: Int) {
                if(viewModel.isOrderComplete().get()!!) {
                    container!!.findNavController().navigate(
                        R.id.action_orderPickupFragment_to_pickupSuccessFragment,
                        bundleOf("order" to requireArguments().getString("order"))
                    )
                }
            }
        })

        fuckWithNfc()

        return binding.root
    }

    private fun fuckWithNfc() {
//        var context = AcmeApplication.getAppContext()
//        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
//        Log.e("NFC supported", (nfcAdapter != null).toString())
//        Log.e("NFC enabled", (nfcAdapter?.isEnabled).toString())
//
//        // NFC not supported
//        if (nfcAdapter == null) {
//            binding.otherPaymentMethod.visibility = View.GONE
//            if(nfcAdapter?.isEnabled != null || nfcAdapter?.isEnabled!!) {
//                // NFC not enabled
//
//            } else {
//                // NFC enabled
//
//            }
//        }
//
//        nfcManager = OutcomingNfcManager(this)
//        nfcAdapter?.setOnNdefPushCompleteCallback(nfcManager, MainActivity.getActivity())
//        nfcAdapter?.setNdefPushMessageCallback(nfcManager, MainActivity.getActivity())

        if(nfcAdapter == null || !nfcAdapter.isEnabled) {
            // NFC Not supported
            ShowFeedback.makeSnackbar("NFC is not supported. Bye!")
        }
        else {
            //nfcAdapter!!.setNdefPushMessage(createNdefMessage(null), MainActivity.getActivity())

            //nfcAdapter.enableForegroundNdefPush(MainActivity.getActivity())
            //nfcAdapter!!.setNdefPushMessageCallback(this, MainActivity.getActivity())
            //nfcAdapter!!.setOnNdefPushCompleteCallback(this, MainActivity.getActivity())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        nfcAdapter!!.setNdefPushMessageCallback(null, MainActivity.getActivity())
        nfcAdapter!!.setOnNdefPushCompleteCallback(null, MainActivity.getActivity())
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

        val outString = "Teste YAP"
//        val ndefRecord = NdefRecord.createTextRecord("en", outString)
//
//        return NdefMessage(ndefRecord)

        val langBytes = "en".toByteArray(Charset.forName("US-ASCII"))
        val utfEncoding = Charset.forName("UTF-8")
        val textBytes = outString.toByteArray(utfEncoding)
        val utfBit = 0
        val status = (utfBit + langBytes.size).toChar()
        val data = ByteArray(1 + langBytes.size + textBytes.size)
        data[0] = status.toByte()
        System.arraycopy(langBytes, 0, data, 1, langBytes.size)
        System.arraycopy(textBytes, 0, data, 1 + langBytes.size, textBytes.size)
        return NdefMessage(NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), data))

    }

    override fun onNdefPushComplete(event: NfcEvent?) {
        ShowFeedback.makeSnackbar("Message transmitted")
    }
}