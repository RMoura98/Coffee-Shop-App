package com.feup.cmov.acme_client.screens.orders.view_order

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.Order
import com.feup.cmov.acme_client.databinding.FragmentSettingsBinding
import com.feup.cmov.acme_client.databinding.FragmentViewOrderBinding
import com.feup.cmov.acme_client.screens.settings.SettingsHandler
import com.feup.cmov.acme_client.screens.settings.SettingsViewModel
import com.feup.cmov.acme_client.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewOrderFragment : Fragment(), ViewOrderHandler {

    private val viewModel : ViewOrderViewModel by viewModels()
    lateinit var binding: FragmentViewOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val order = Order.deserialize(requireArguments().getString("order")!!)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_view_order, container, false
        )

        binding.viewModel = viewModel
        binding.handler = this

        if(order.completed) {
            with(binding.orderCompletedIcon) {
                setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                setColorFilter(ContextCompat.getColor(AcmeApplication.getAppContext(), R.color.green_600), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            binding.orderCompletedStatus.text = "Order #${order.order_sequential_id}"
            binding.orderCompletedCaption.text = "Let the flavors and aromatics of ACME coffee take you to a new dimension."
            //binding.orderPlacedOn.text = order.formatCreationDate()
        }
        else {
            with(binding.orderCompletedIcon) {
                setImageResource(R.drawable.ic_outline_play_circle_outline_24)
                setColorFilter(ContextCompat.getColor(AcmeApplication.getAppContext(), R.color.orange_600), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            binding.orderCompletedStatus.text = "Order Pending"
            binding.orderCompletedCaption.text = "Please pickup your order at the counter of ACME Coffee Shop."
            binding.orderPlacedOn.text = "€" + order.formatCreationDate()
        }

        binding.orderTotal.text = String.format("%.2f", order.total)

        binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        return binding.root
    }

}