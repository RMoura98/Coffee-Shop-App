package com.feup.cmov.acme_terminal.screens.order_history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.feup.cmov.acme_terminal.R
import com.feup.cmov.acme_terminal.database.models.OrderWithItems
import com.feup.cmov.acme_terminal.databinding.FragmentOrdersHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersHistoryFragment : Fragment(), OrdersHistoryHandler {

    private val viewModel: OrdersHistoryViewModel by activityViewModels()
    lateinit var binding: FragmentOrdersHistoryBinding
    var adapter: OrderItemAdapter =  OrderItemAdapter(this)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders_history, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if(arguments?.getString("changeBackButton") == null) {
                isEnabled = false
                activity?.onBackPressed()
            } else
                container!!.findNavController()
                    .navigate(R.id.action_orderHistoryFragment_to_scannerFragment)
        }

        val toolbar = binding.topAppBar

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        binding.orderHistoryOrdersItems.adapter = adapter
        viewModel.getAllOrders()
        viewModel.getAllOrdersLD().observe(viewLifecycleOwner) { items ->
            adapter.data = items
        }

        return binding.root
    }

    override fun viewOrder(v: View, order: OrderWithItems) {
        v.findNavController()
            .navigate(
                R.id.action_orderHistoryFragment_to_orderDetailsFragment,
                bundleOf("order" to OrderWithItems.serialize(order))
            )
    }

    override fun wasAnOrderAdded() = arguments?.getString("changeBackButton") != null
}