package com.feup.cmov.acme_client.screens.settings.profile

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
import dagger.hilt.android.AndroidEntryPoint


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

        val toolbar = binding.topAppBar

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        return binding.root
    }
}