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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreFragment : Fragment(), StoreHandler {

    private val viewModel: StoreViewModel by viewModels()
    lateinit var binding: FragmentStoreBinding
    private lateinit var adapter: MenuItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_store, container, false
        )

        viewModel.getMenuItems().observe(viewLifecycleOwner, Observer observe@{ menuItems ->
            System.out.println("Loaded menus")
            System.out.println(menuItems.size)
            adapter.data = menuItems
        });

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MenuItemAdapter()
        binding.mainMenuFragmentItemsList.adapter = adapter
    }

}