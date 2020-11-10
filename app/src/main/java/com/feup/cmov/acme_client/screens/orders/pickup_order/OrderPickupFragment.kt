package com.feup.cmov.acme_client.screens.orders.pickup_order

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.comm.Packet
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.databinding.FragmentOrderPickupBinding
import com.feup.cmov.acme_client.utils.Measurements
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.glxn.qrgen.android.QRCode


@AndroidEntryPoint
class OrderPickupFragment : Fragment() {

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

        val qrCode: Bitmap =
            QRCode.from("${packet.signature},${packet.payloadString}").withSize(
                Measurements.convertDptoPx(360).toInt(),
                Measurements.convertDptoPx(360).toInt()
            ).withErrorCorrection(
                ErrorCorrectionLevel.Q
            ).bitmap()
        val merge = mergeBitmaps(BitmapFactory.decodeResource(resources, R.drawable.coffee_trimmed), qrCode)
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

        return binding.root
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
}