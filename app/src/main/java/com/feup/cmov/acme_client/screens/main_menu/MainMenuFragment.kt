package com.feup.cmov.acme_client.screens.main_menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.screens.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class MainMenuFragment : Fragment(), MainMenuHandler {

    private val viewModel: MainMenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Snackbar.make(container!!, "Boom logged in", Snackbar.LENGTH_LONG).show();

        viewModel.getMenuItems().observe(viewLifecycleOwner, Observer observe@{ menuItems ->
            System.out.println("Loaded menus")
            System.out.println(menuItems.size)
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

}