package com.feup.cmov.acme_terminal.screens.order_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_terminal.AcmeApplication
import com.feup.cmov.acme_terminal.R
import com.feup.cmov.acme_terminal.database.models.OrderWithItems
import com.feup.cmov.acme_terminal.databinding.FragmentOrderDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil
import kotlin.math.floor

@AndroidEntryPoint
class OrderDetailsFragment: Fragment(), OrderDetailsHandler {

    private val viewModel: OrderDetailsViewModel by activityViewModels()
    lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var orderWithItems: OrderWithItems
    private var cartItemAdapter: CartItemAdapter = CartItemAdapter()
    private var voucherUsedAdapter: VoucherUsedAdapter = VoucherUsedAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false)

//        viewModel.order.observe(viewLifecycleOwner, Observer {
//            println("Order: $it")
//        })

        orderWithItems = viewModel.order.value!!

        val toolbar = binding.topAppBar

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed();
        }

        binding.cartItemList.adapter = cartItemAdapter
        binding.cartVoucherList.adapter = voucherUsedAdapter

        inflateView()

        return binding.root
    }

    private fun inflateView() {
        binding.topAppBar.title =
                "Order #" + orderWithItems.order.order_sequential_id + " Receipt"


        inflateInformationSection()

        val totalPrice: Float = orderWithItems.order.total

        val (sumAllItemsPrice,
                orderItems,
                voucherItems
        ) = getData()

        cartItemAdapter.data = orderItems.values.toList()

        if (voucherItems.isEmpty()) {
            binding.cartVoucherList.visibility = View.GONE
        } else {
            binding.noVoucherText.visibility = View.GONE
            voucherUsedAdapter.data = voucherItems
        }

        inflateOrderSummary(sumAllItemsPrice, totalPrice)
    }

    private fun inflateInformationSection() {
        var iconColor: Int
        var iconSrcId: Int
        if (orderWithItems.order.completed) {
            iconColor = R.color.green_600
            iconSrcId = R.drawable.ic_baseline_check_circle_outline_24
            binding.orderCompletedStatus.text = "Completed"
            binding.orderCompletedDateInformation.text = orderWithItems.order.formatCompletedDate()
        } else {
            iconColor = R.color.orange_600
            iconSrcId = R.drawable.ic_outline_play_circle_outline_24
            binding.orderCompletedStatus.text = "Pending"
            binding.dateCompletedSection.visibility = View.GONE
        }

        binding.orderDateInformation.text = orderWithItems.order.formatCreationDate()

        binding.customerName.text = orderWithItems.order.user

        with(binding.orderCompletedIcon) {
            setImageResource(iconSrcId)
            setColorFilter(
                    androidx.core.content.ContextCompat.getColor(
                            AcmeApplication.getAppContext(),
                            iconColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun inflateOrderSummary(subTotal: Float, totalPrice: Float) {
        val priceStringFormat: String =
                AcmeApplication.getAppContext().getString(R.string.cart_price)
        var total = floor(totalPrice * 100) / 100
        val savings = subTotal - total

        binding.subtotalPrice.text = priceStringFormat.format(subTotal)
        binding.voucherPrice.text = priceStringFormat.format(savings)
        binding.totalPrice.text = priceStringFormat.format(total)
    }

    private fun getData(): Triple<Float ,MutableMap<Long, CartItemAdapter.CartItem>,  ArrayList<VoucherUsedAdapter.VoucherWithSavings>> {
        var sumAllItemsPrice = 0f
        var coffeePrice = 0f
        var allItems: MutableMap<Long, CartItemAdapter.CartItem> = mutableMapOf()
        for (orderItem in orderWithItems.orderItems) {
            val quantity = orderItem.orderItem.quantity.toInt()
            val menuItem = orderItem.menuItem

            if (menuItem.name == "Coffee") coffeePrice = menuItem.price

            allItems[menuItem.id] = CartItemAdapter.CartItem(menuItem, quantity)
            sumAllItemsPrice += menuItem.price * quantity
        }

        val countCoffeeVouchers = orderWithItems.vouchers.count { it.voucherType == "free_coffee" }
        val voucherWithSavingsList = ArrayList<VoucherUsedAdapter.VoucherWithSavings>()
        for (voucher in orderWithItems.vouchers) {
            if (voucher.voucherType == "free_coffee")
                voucherWithSavingsList.add(VoucherUsedAdapter.VoucherWithSavings(voucher,coffeePrice))
            else if (voucher.voucherType == "discount") {
                var savings = (sumAllItemsPrice - (coffeePrice * countCoffeeVouchers)) * 0.05F
                savings = floor(savings * 100) / 100 // round to floor with 2 decimal places
                voucherWithSavingsList.add(VoucherUsedAdapter.VoucherWithSavings(voucher, savings))
            }
        }

        return Triple(sumAllItemsPrice, allItems, voucherWithSavingsList)
    }

}