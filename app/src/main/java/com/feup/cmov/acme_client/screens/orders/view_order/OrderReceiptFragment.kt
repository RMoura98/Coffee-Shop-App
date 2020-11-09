package com.feup.cmov.acme_client.screens.orders.view_order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import com.feup.cmov.acme_client.databinding.FragmentOrderReceiptBinding
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import com.feup.cmov.acme_client.screens.checkout.cart.CartItemAdapter
import com.feup.cmov.acme_client.screens.checkout.cart.VoucherUsedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil
import kotlin.math.floor

@AndroidEntryPoint
class OrderReceiptFragment : Fragment() {

    private lateinit var binding: FragmentOrderReceiptBinding
    private lateinit var orderWithItems: OrderWithItems
    private var cartItemAdapter: CartItemAdapter = CartItemAdapter(false)
    private var voucherUsedAdapter: VoucherUsedAdapter = VoucherUsedAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_receipt, container, false
        )

        orderWithItems = OrderWithItems.deserialize(requireArguments().getString("order")!!)

        Log.d("order", orderWithItems.order.toString())
        Log.d("Items", orderWithItems.orderItems.toString())
        Log.d("vouchers", orderWithItems.vouchers.toString())


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
        if (orderWithItems.order.completed)
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

    private fun inflateOrderSummary(subTotalPrice: Float, totalPrice: Float) {
        val priceStringFormat: String =
            AcmeApplication.getAppContext().getString(R.string.cart_price)
        val savings = subTotalPrice - totalPrice

        binding.subtotalPrice.text = priceStringFormat.format(subTotalPrice)
        binding.voucherPrice.text = priceStringFormat.format(savings)
        binding.totalPrice.text = priceStringFormat.format(totalPrice)
    }

    private fun getData(): Triple<Float ,MutableMap<Long, CartViewModel.CartItem>,  ArrayList<VoucherUsedAdapter.VoucherWithSavings>> {
        var sumAllItemsPrice = 0f
        var coffeePrice = 0f
        var allItems: MutableMap<Long, CartViewModel.CartItem> = mutableMapOf()
        for (orderItem in orderWithItems.orderItems) {
            val quantity = orderItem.orderItem.quantity.toInt()
            val menuItem = orderItem.menuItem

            if (menuItem.name == "Coffee") coffeePrice = menuItem.price

            allItems[menuItem.id] = CartViewModel.CartItem(menuItem, quantity)
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