package com.feup.cmov.acme_client.screens.orders.pickup_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.databinding.FragmentPickupSuccessBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PickupSuccessFragment : Fragment() {

    lateinit var binding: FragmentPickupSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pickup_success, container, false
        )

        val orderWithItems = OrderWithItems.deserialize(requireArguments().getString("order")!!)
        val voucherListType = object : TypeToken<List<Voucher>>() {}.type
        val earnedVouchers = Gson().fromJson<List<Voucher>>(requireArguments().getString("earnedVouchers"), voucherListType)

        val vouchers = ArrayList<Voucher>()

        for(earnedVoucher: Voucher in earnedVouchers){
            vouchers.add(earnedVoucher)
            println(vouchers)
        }

        if(vouchers.isEmpty()) {
            binding.caption.text = "Enjoy your coffee while it's hot!"
        }
        else {
            if(vouchers.size == 1)
                binding.caption.text = "Congratulations! You have received a voucher!"
            else
                binding.caption.text = "Congratulations! You have received some vouchers!"
            val adapter = ReceivedVoucherAdapter()
            adapter.data = vouchers
            binding.voucherList.adapter = adapter
        }

        val progressBar = binding.progressBar

        GlobalScope.launch {
            val animationTime = 3700 // milliseconds
            for (i in 1..100) {
                delay((animationTime / 100).toLong())
                progressBar.progress = i
            }

            delay(150)
            container!!.findNavController()
                .navigate(
                    R.id.action_pickupSuccessFragment_to_viewOrderFragment, bundleOf(
                        "order" to requireArguments().getString(
                            "order"
                        )
                    )
                )

        }

        return binding.root
    }
}