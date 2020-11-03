package com.feup.cmov.acme_client.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentProfileBinding
import com.feup.cmov.acme_client.databinding.FragmentSettingsBinding
import com.feup.cmov.acme_client.databinding.FragmentSignupBinding
import com.feup.cmov.acme_client.utils.PreferencesUtils
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class SettingsFragment : Fragment(), SettingsHandler {

    private val viewModel : SettingsViewModel by viewModels()
    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings, container, false
        )

        binding.viewModel = viewModel
        binding.handler = this

        return binding.root
    }

    override fun onProfileButtonClick(v: View) {
        v.findNavController()
            .navigate(R.id.action_mainMenuFragment_to_profileFragment)
    }

    override fun onLogoutButtonClick(v: View) {
        PreferencesUtils.logoutUser()
        v.findNavController()
            .navigate(R.id.action_mainMenuFragment_to_loginFragment)
    }


}