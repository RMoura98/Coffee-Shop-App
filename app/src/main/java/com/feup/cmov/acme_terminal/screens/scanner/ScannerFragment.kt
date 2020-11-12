package com.feup.cmov.acme_terminal.screens.scanner

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.feup.cmov.acme_terminal.AcmeApplication
import com.feup.cmov.acme_terminal.R
import com.feup.cmov.acme_terminal.database.models.Order
import com.feup.cmov.acme_terminal.database.models.OrderData
import com.feup.cmov.acme_terminal.database.models.OrderWithItems
import com.feup.cmov.acme_terminal.databinding.FragmentScannerBinding
import com.feup.cmov.acme_terminal.screens.order_details.OrderDetailsViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ScannerFragment : Fragment(), ScannerHandler {

    private val viewModel: OrderDetailsViewModel by activityViewModels()
    lateinit var binding: FragmentScannerBinding
    lateinit var integrator: IntentIntegrator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scanner, container, false)

        binding.handler = this

        // Setup QR Code Scanner
        integrator = IntentIntegrator.forSupportFragment(this)

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        Log.e("result", result.toString())
        if (result != null && result.contents != null && result.formatName == "QR_CODE") {
            println(result.contents)

            val splitContents = result.contents.split(",", limit = 2);
            val signature = splitContents[0]
            val orderJson = splitContents[1]

            val gson = Gson()
            val order: OrderData = gson.fromJson(orderJson, OrderData::class.java)

            viewModel.placeOrder(order, signature)

            viewModel.order.observe(viewLifecycleOwner, Observer {
                requireView().findNavController().navigate(R.id.action_scannerFragment_to_orderDetailsFragment);
            })
        }

    }

    override fun onScanOrderButtonClick(v: View) {
        integrator.initiateScan()
    }
}