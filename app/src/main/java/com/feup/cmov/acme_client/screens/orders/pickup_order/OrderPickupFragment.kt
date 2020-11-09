package com.feup.cmov.acme_client.screens.orders.pickup_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.databinding.FragmentOrderPickupBinding
import com.feup.cmov.acme_client.databinding.FragmentPickupSuccessBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OrderPickupFragment : Fragment() {

    lateinit var binding: FragmentOrderPickupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_pickup, container, false
        )

        val orderWithItems = OrderWithItems.deserialize(requireArguments().getString("order")!!)

        GlobalScope.launch {
            delay(7500)
            container!!.findNavController().navigate(
                R.id.action_orderPickupFragment_to_pickupSuccessFragment,
                bundleOf("order" to requireArguments().getString("order"))
            )
        }

        binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        return binding.root
    }
}