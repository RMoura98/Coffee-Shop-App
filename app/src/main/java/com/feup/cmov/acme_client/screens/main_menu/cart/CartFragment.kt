package com.feup.cmov.acme_client.screens.main_menu.cart

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentCartBinding
import com.feup.cmov.acme_client.network.Result
import com.feup.cmov.acme_client.network.responses.PlaceOrderResponse
import com.feup.cmov.acme_client.screens.main_menu.CartViewModel
import com.feup.cmov.acme_client.screens.main_menu.MainMenuFragment
import com.feup.cmov.acme_client.screens.settings.vouchers.VoucherAdapter
import com.feup.cmov.acme_client.utils.ShowFeedback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment() : Fragment(), CartHandler {

    private val viewModel: CartViewModel by activityViewModels()
    lateinit var binding: FragmentCartBinding
    private var cartItemAdapter: CartItemAdapter = CartItemAdapter()
    private var voucherUsedAdapter: VoucherUsedAdapter = VoucherUsedAdapter()
    private lateinit var mainMenu: MainMenuFragment

    constructor(mainMenu: MainMenuFragment): this() {
        this.mainMenu = mainMenu
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cart, container, false
        )

        // Setting binding params
        binding.viewModel = viewModel
        binding.handler = this

        val toolbar = binding.checkoutTopAppBar

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        binding.seeMenu.setOnClickListener { activity?.onBackPressed() }

        viewModel.getCartListLiveData().observe(viewLifecycleOwner, Observer observe@{ cartList ->
            cartItemAdapter.data = cartList.values.toList()
        });

        val priceStringFormat: String = AcmeApplication.getAppContext().getString(R.string.cart_price)
        viewModel.getTotalCartPrice().observe(viewLifecycleOwner, Observer observe@{ totalCartPrice ->
            binding.subtotalPrice.text = String.format(priceStringFormat, totalCartPrice)
            binding.totalPriceNextButton.text = String.format(priceStringFormat, totalCartPrice) // isto tem de estar noutro sitio
        })

        viewModel.getSelectedVouchers().observe(viewLifecycleOwner, Observer observe@{ vouchersList ->
            if (vouchersList.isEmpty()) {
                binding.noVoucherText.visibility = View.VISIBLE
                binding.cartVoucherList.visibility = View.GONE
            } else {
                binding.noVoucherText.visibility = View.GONE
                binding.cartVoucherList.visibility = View.VISIBLE
            }

            // Calculate Savings Per Voucher
            voucherUsedAdapter.data = viewModel.getSavingsForSelectedVouchers()
        })

        viewModel.isOrderPlaced().observe(viewLifecycleOwner, Observer observe@{ result ->
            if(viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED)
                return@observe

            when(result) {
                is Result.Success -> {
                    ShowFeedback.makeSnackbar("Success")
                }
                is Result.NetworkError -> {
                    ShowFeedback.makeSnackbar("No internet connection")
                }
                is Result.OtherError -> {
                    ShowFeedback.makeSnackbar(result.msg)
                }
            }
        });

        val totalSavings = viewModel.getTotalSavings()
        binding.voucherPrice.text = (if(totalSavings > 0) "- " else "") +
                String.format(priceStringFormat, totalSavings)

        val totalPrice = viewModel.getTotalCartPrice().value!! - totalSavings
        binding.totalPrice.text = String.format(priceStringFormat, totalPrice)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cartItemList.adapter = cartItemAdapter
        binding.cartVoucherList.adapter = voucherUsedAdapter
    }

    override fun onAddVoucherClick(v: View) {
        v.findNavController()
            .navigate(R.id.action_cartFragment_to_voucherSelectionFragment)
    }

    override fun placeOrder(v: View) {
        viewModel.completeOrder()
    }

}