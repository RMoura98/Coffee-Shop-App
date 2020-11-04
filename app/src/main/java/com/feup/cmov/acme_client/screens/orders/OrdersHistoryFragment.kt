package com.feup.cmov.acme_client.screens.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentOrdersHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersHistoryFragment : Fragment(), OrdersHistoryHandler {

    private val viewModel: OrdersHistoryViewModel by viewModels()
    lateinit var binding: FragmentOrdersHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_orders_history, container, false
        )

        binding.viewModel = viewModel
        binding.handler = this

        return binding.root
    }

}