package com.feup.cmov.acme_client.screens.main_menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentLoginBinding
import com.feup.cmov.acme_client.databinding.FragmentMainMenuBinding
import com.feup.cmov.acme_client.databinding.FragmentSignupBinding
import com.feup.cmov.acme_client.screens.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class MainMenuFragment : Fragment(), MainMenuHandler {

    private val viewModel: MainMenuViewModel by viewModels()
    lateinit var binding: FragmentMainMenuBinding
    private lateinit var adapter: MenuItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main_menu, container, false
        )
        Snackbar.make(container!!, "Boom logged in", Snackbar.LENGTH_LONG).show();

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