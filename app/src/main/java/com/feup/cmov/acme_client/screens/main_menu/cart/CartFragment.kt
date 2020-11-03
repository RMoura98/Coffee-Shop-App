package com.feup.cmov.acme_client.screens.main_menu.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import com.feup.cmov.acme_client.database.models.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentCartBinding
import com.feup.cmov.acme_client.databinding.FragmentStoreBinding
import com.feup.cmov.acme_client.screens.main_menu.MainMenuFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment() : Fragment(), CartHandler {

    private val viewModel: CartViewModel by viewModels()
    lateinit var binding: FragmentCartBinding
    private lateinit var adapter: CartItemAdapter
    private lateinit var mainMenu: MainMenuFragment

    constructor(mainMenu: MainMenuFragment): this() {
        adapter = CartItemAdapter()
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
//        Log.d("DEBUG: SIS", (savedInstanceState == null).toString())
//        Log.d("DEBUG: scrollY", savedInstanceState?.getInt("scrollY").toString())
//        binding.nestedScrollView.scrollY = savedInstanceState?.getInt("scrollY") ?: 0



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainMenuFragmentItemsList.adapter = adapter
    }

    fun setAdapterData(cartItems: List<MenuItem>) {
        adapter.data = cartItems
    }

}