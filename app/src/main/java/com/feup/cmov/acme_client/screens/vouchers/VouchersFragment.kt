package com.feup.cmov.acme_client.screens.vouchers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.databinding.FragmentVouchersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VouchersFragment : Fragment(), VouchersHandler {

    private val viewModel : VouchersViewModel by viewModels()
    lateinit var binding: FragmentVouchersBinding
    private lateinit var adapter: VoucherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_vouchers, container, false
        )

        // Setting binding params
        binding.viewModel = viewModel
        binding.handler = this

        viewModel.getUnusedVouchers().observe(viewLifecycleOwner, Observer { vouchers ->
            println("Loaded vouchers: " + vouchers.size)
            adapter.data = vouchers
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = VoucherAdapter()
        binding.vouchersFragmentVouchersList.adapter = adapter
    }
}