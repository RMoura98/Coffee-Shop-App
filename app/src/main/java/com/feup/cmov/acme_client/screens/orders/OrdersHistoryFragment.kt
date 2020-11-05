package com.feup.cmov.acme_client.screens.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentOrdersHistoryBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersHistoryFragment : Fragment(), OrdersHistoryHandler {

    private val viewModel: OrdersHistoryViewModel by viewModels()
    lateinit var binding: FragmentOrdersHistoryBinding
    var adapter: OrderItemAdapter =  OrderItemAdapter(this)

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

        viewModel.getOrders().observe(viewLifecycleOwner) { items ->
            adapter.data = items

            var notCompleted = 0
            for(item in items){
                if(!item.order.completed)
                    notCompleted++
            }

            if(notCompleted > 0) {
                val tab = binding.orderTabLayout.getTabAt(1)!!
                tab.getOrCreateBadge()
            }
        };

        binding.orderTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab != null && tab.position == 0) {
                    adapter.showing = OrderItemAdapter.SHOWING.COMPLETED_ORDERS
                }
                else
                    adapter.showing = OrderItemAdapter.SHOWING.ONGOING_ORDERS
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                onTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderHistoryOrdersItems.adapter = adapter

        if(adapter.showing == OrderItemAdapter.SHOWING.COMPLETED_ORDERS)
            binding.orderTabLayout.getTabAt(0)!!.select()
        else
            binding.orderTabLayout.getTabAt(1)!!.select()
    }

}