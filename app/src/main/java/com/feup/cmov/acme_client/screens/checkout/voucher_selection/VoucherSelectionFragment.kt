package com.feup.cmov.acme_client.screens.settings.vouchers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.databinding.FragmentVoucherSelectionBinding
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import com.feup.cmov.acme_client.screens.checkout.voucher_selection.VoucherSelectionAdapter
import com.feup.cmov.acme_client.screens.checkout.voucher_selection.VoucherSelectionHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class VoucherSelectionFragment : Fragment(), VoucherSelectionHandler {

    private val viewModel : CartViewModel by activityViewModels()
    lateinit var binding: FragmentVoucherSelectionBinding
    lateinit var adapter: VoucherSelectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adapter = VoucherSelectionAdapter(viewModel, this)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_voucher_selection, container, false
        )

        // Setting binding params
        binding.handler = this

        viewModel.getVouchers().observe(viewLifecycleOwner, Observer { vouchers ->
            adapter.data = vouchers.filter { voucher -> !voucher.used }
        })

        val toolbar = binding.topAppBar
        toolbar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        viewModel.getSelectedVouchers().observe(viewLifecycleOwner, Observer { vouchers ->
            if(!vouchers.isEmpty()) {
                binding.topAppBar.title = "${vouchers.size} ${if(vouchers.size == 1) "voucher" else "vouchers"} selected"
                toolbar.setNavigationIcon(R.drawable.ic_baseline_check_24)
            } else {
                binding.topAppBar.title = "Select Vouchers"
                toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.vouchersFragmentVouchersList.adapter = adapter
        binding.vouchersFragmentVouchersList.isNestedScrollingEnabled = false
    }

    override fun onCheckboxTick(voucher: Voucher, checked: Boolean) {
        if(checked)
            viewModel.selectVoucher(voucher)
        else
            viewModel.unselectVoucher(voucher)
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                for((position, voucher) in adapter.data.withIndex()) {
                    if(voucher.voucherType == "discount")
                        adapter.notifyItemChanged(position)
                    else if(voucher.voucherType == "free_coffee") {
                        if(viewModel.countCoffeVouchersSelected() == viewModel.countCoffesInCart() || viewModel.countCoffeVouchersSelected() == viewModel.countCoffesInCart() - 1) {
                            adapter.notifyItemChanged(position)
                        }
                    }
                }
            }
        }
    }
}