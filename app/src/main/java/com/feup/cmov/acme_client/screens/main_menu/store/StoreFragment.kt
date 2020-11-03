package com.feup.cmov.acme_client.screens.main_menu.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentStoreBinding
import com.feup.cmov.acme_client.screens.main_menu.MainMenuFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreFragment(private var mainMenu: MainMenuFragment) : Fragment(), StoreHandler {

    private val viewModel: StoreViewModel by viewModels()
    lateinit var binding: FragmentStoreBinding
    private lateinit var adapter: MenuItemAdapter

//    constructor(mainMenu: MainMenuFragment) : this() {
//        this.mainMenu = mainMenu
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_store, container, false
        )
//        Log.d("DEBUG: SIS", (savedInstanceState == null).toString())
//        Log.d("DEBUG: scrollY", savedInstanceState?.getInt("scrollY").toString())
//        binding.nestedScrollView.scrollY = savedInstanceState?.getInt("scrollY") ?: 0

        mainMenu.getMenuItemsLiveData().observe(viewLifecycleOwner, Observer observe@{ menuItems ->
            System.out.println("Loaded menus")
            System.out.println(menuItems.size)
            adapter.data = menuItems
        });

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MenuItemAdapter(this)
        binding.mainMenuFragmentItemsList.adapter = adapter
    }

    override fun addToCartOnClick(id: Long) {
        mainMenu.addItemToCart(id)
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        Log.d("DEBUG: onSIS", "agora")
//        outState.putInt("scrollY", binding.mainMenuFragmentItemsList.scrollY)
//    }

    companion object {
        fun newInstance(mainMenu: MainMenuFragment): StoreFragment {
            return StoreFragment(mainMenu)
        }
    }
}