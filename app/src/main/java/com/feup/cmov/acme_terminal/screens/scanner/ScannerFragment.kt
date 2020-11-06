package com.feup.cmov.acme_terminal.screens.scanner

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.feup.cmov.acme_terminal.R
import com.feup.cmov.acme_terminal.databinding.FragmentScannerBinding
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ScannerFragment : Fragment(), ScannerHandler {

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

        if (result != null) {
            println(result.contents)
            // TODO: do something with content
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onScanOrderButtonClick(v: View) {
        integrator.initiateScan()
    }
}