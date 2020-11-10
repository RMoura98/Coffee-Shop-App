package com.feup.cmov.acme_client.screens.orders.pickup_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.databinding.FragmentOrderPlacedBinding
import com.feup.cmov.acme_client.databinding.FragmentPickupSuccessBinding
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import com.feup.cmov.acme_client.screens.checkout.order_placed.OrderPlacedHandler
import com.feup.cmov.acme_client.screens.main_menu.MainMenuFragment
import com.feup.cmov.acme_client.screens.main_menu.MainMenuViewModel
import com.feup.cmov.acme_client.screens.orders.OrdersHistoryFragment
import com.feup.cmov.acme_client.screens.settings.vouchers.VoucherAdapter
import com.feup.cmov.acme_client.utils.PreferencesUtils
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

        val vouchers = ArrayList<Voucher>()
//        vouchers.add(Voucher( "123", orderWithItems.order.order_id, "free_coffee", orderWithItems.order.order_id))
//        vouchers.add(Voucher( "124", orderWithItems.order.order_id, "discount", orderWithItems.order.order_id))

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
                delay( (animationTime / 100).toLong() )
                progressBar.progress = i
            }

            delay(150)
            container!!.findNavController()
                .navigate(R.id.action_pickupSuccessFragment_to_viewOrderFragment, bundleOf("order" to requireArguments().getString("order")))

        }

        return binding.root
    }
}