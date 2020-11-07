package com.feup.cmov.acme_client.screens.orders.view_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentSettingsBinding
import com.feup.cmov.acme_client.databinding.FragmentViewOrderBinding
import com.feup.cmov.acme_client.screens.settings.SettingsHandler
import com.feup.cmov.acme_client.screens.settings.SettingsViewModel
import com.feup.cmov.acme_client.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewOrderFragment : Fragment(), ViewOrderHandler {

    private val viewModel : ViewOrderViewModel by viewModels()
    lateinit var binding: FragmentViewOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_view_order, container, false
        )

        binding.viewModel = viewModel
        binding.handler = this

        binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.topAppBar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        return binding.root
    }

}