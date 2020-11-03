package com.feup.cmov.acme_client.screens.settings.payment_method

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentPaymentMethodBinding
import com.feup.cmov.acme_client.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentMethodFragment : Fragment(), PaymentMethodHandler {

    private val viewModel : PaymentMethodViewModel by viewModels()
    lateinit var binding: FragmentPaymentMethodBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment_method, container, false
        )

        // Setting binding params
        binding.viewModel = viewModel
        binding.handler = this

        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            // Format fields
            viewModel.updateFormatFields()
            // Force bindings to update
            binding.invalidateAll()
        })

        val toolbar = binding.topAppBar

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        return binding.root
    }
}