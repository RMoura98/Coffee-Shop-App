package com.feup.cmov.acme_terminal.screens.order_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_terminal.R
import com.feup.cmov.acme_terminal.databinding.FragmentOrderDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment: Fragment(), OrderDetailsHandler {

    private val viewModel: OrderDetailsViewModel by activityViewModels()
    lateinit var binding: FragmentOrderDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false)

        viewModel.order.observe(viewLifecycleOwner, Observer {
            println("Order: $it")
        })

        return binding.root
    }
}