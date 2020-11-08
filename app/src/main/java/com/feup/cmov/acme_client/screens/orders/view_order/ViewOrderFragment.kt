package com.feup.cmov.acme_client.screens.orders.view_order

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.databinding.FragmentViewOrderBinding
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import com.feup.cmov.acme_client.screens.main_menu.MainMenuFragment
import com.feup.cmov.acme_client.screens.main_menu.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewOrderFragment : Fragment(), ViewOrderHandler {

    private val viewModel : ViewOrderViewModel by viewModels()
    lateinit var binding: FragmentViewOrderBinding
    lateinit var orderWithItems: OrderWithItems

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        orderWithItems = OrderWithItems.deserialize(requireArguments().getString("order")!!)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_view_order, container, false
        )

        binding.viewModel = viewModel
        binding.handler = this

        if(orderWithItems.order.completed) {
            with(binding.orderCompletedIcon) {
                setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                setColorFilter(ContextCompat.getColor(AcmeApplication.getAppContext(), R.color.green_600), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            binding.orderCompletedStatus.text = "Order #${orderWithItems.order.order_sequential_id}"
            binding.orderCompletedCaption.text = "Let the flavors and aromatics of ACME coffee take you to a new dimension."
            binding.orderCompletedOnLayout.visibility = View.VISIBLE
            binding.orderCompletedOn.text = orderWithItems.order.formatCompletedDate()

            binding.pickUpOrderButton.visibility = View.GONE
            binding.cancelOrderButton.visibility = View.GONE
            binding.orderSummarySeparator.visibility = View.VISIBLE
        }
        else {
            with(binding.orderCompletedIcon) {
                setImageResource(R.drawable.ic_outline_play_circle_outline_24)
                setColorFilter(ContextCompat.getColor(AcmeApplication.getAppContext(), R.color.orange_600), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            binding.orderCompletedStatus.text = "Order Pending"
            binding.orderCompletedCaption.text = "Please pickup your order at the counter of ACME Coffee Shop."
        }

        binding.orderTotal.text = "€" + String.format("%.2f", orderWithItems.order.total)
        binding.numberOfItems.text = orderWithItems.getNumberOfItemsBought().toString()
        binding.orderPlacedOn.text = orderWithItems.order.formatCreationDate()

        binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        viewModel.wasOrderDeleted().observe(viewLifecycleOwner, Observer observe@{ wasOrderDeleted ->
            if(wasOrderDeleted) {
                activity?.onBackPressed();
            }
        });

        return binding.root
    }

    override fun clickPickupOrder(v: View) {
        TODO("Not yet implemented")
    }

    override fun clickOrderReceipt(v: View) {
        TODO("Not yet implemented")
    }

    override fun clickRemakeOrder(v: View) {
        val cartViewModel: CartViewModel by activityViewModels()
        val mainMenuViewModel: MainMenuViewModel by activityViewModels()

        cartViewModel.clearViewModel()
        for (orderItem in orderWithItems.orderItems){
            for(i in 0 until orderItem.orderItem.quantity)
                cartViewModel.addItemToCart(orderItem.menuItem)
        }
        mainMenuViewModel.setCurrentAction(R.id.storeAction)
        MainMenuFragment.hasShownCartAnimation = true
        v.findNavController().navigate(R.id.action_viewOrderFragment_to_cartFragment)
    }

    override fun clickDeleteOrder(v: View) {
        viewModel.removeOrder(orderWithItems)
    }
}