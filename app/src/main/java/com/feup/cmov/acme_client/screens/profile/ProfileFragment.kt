package com.feup.cmov.acme_client.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentProfileBinding
import com.feup.cmov.acme_client.databinding.FragmentSignupBinding
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class ProfileFragment : Fragment(), ProfileHandler {

    private val viewModel : ProfileViewModel by viewModels()
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile, container, false
        )

        // Setting binding params
        binding.viewModel = viewModel
        binding.handler = this

        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            // Force bindings to update
            binding.invalidateAll()
        })

        return binding.root
    }
}