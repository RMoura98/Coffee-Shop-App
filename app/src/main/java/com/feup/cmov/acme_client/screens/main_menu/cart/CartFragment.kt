package com.feup.cmov.acme_client.screens.main_menu.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentCartBinding
import com.feup.cmov.acme_client.screens.main_menu.CartViewModel
import com.feup.cmov.acme_client.screens.main_menu.MainMenuFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment() : Fragment(), CartHandler {

    private val viewModel: CartViewModel by activityViewModels()
    lateinit var binding: FragmentCartBinding
    private var adapter: CartItemAdapter = CartItemAdapter()
    private lateinit var mainMenu: MainMenuFragment

    constructor(mainMenu: MainMenuFragment): this() {
        this.mainMenu = mainMenu
    }

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
            adapter.data = cartList.values.toList()
        });

        val priceStringFormat: String = AcmeApplication.getAppContext().getString(R.string.cart_price)
        viewModel.getTotalCartPrice().observe(viewLifecycleOwner, Observer observe@{ totalCartPrice ->
            binding.totalPrice.text = String.format(priceStringFormat, totalCartPrice)
            binding.totalPriceNextButton.text = String.format(priceStringFormat, totalCartPrice)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainMenuFragmentItemsList.adapter = adapter
    }

    override fun onAddVoucherClick(v: View) {
        v.findNavController()
            .navigate(R.id.action_cartFragment_to_voucherSelectionFragment)
    }

}