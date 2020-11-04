package com.feup.cmov.acme_client.screens.settings.vouchers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentVouchersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VouchersFragment : Fragment(), VouchersHandler {

    private val viewModel : VouchersViewModel by viewModels()
    lateinit var binding: FragmentVouchersBinding
    private lateinit var adapter: VoucherAdapter
    private lateinit var refreshLayout: SwipeRefreshLayout

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

        val toolbar = binding.topAppBar

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        viewModel.getVouchers().observe(viewLifecycleOwner, Observer { vouchers ->
            adapter.data = vouchers
            refreshLayout.isRefreshing = false
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = VoucherAdapter()
        binding.vouchersFragmentVouchersList.adapter = adapter

        refreshLayout = view.findViewById(R.id.vouchersFragment_swipeRefresh)

        refreshLayout.setOnRefreshListener {
            viewModel.refreshVouchers()
        }
    }
}