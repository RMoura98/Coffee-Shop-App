package com.feup.cmov.acme_client.screens.settings.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.InverseBindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.databinding.FragmentProfileBinding
import com.feup.cmov.acme_client.screens.settings.payment_method.PaymentMethodViewModel
import com.google.android.material.snackbar.Snackbar
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

        viewModel.getUpdateUserResult().observe(viewLifecycleOwner, Observer observe@ {
            clearErrors()

            if (it is ProfileViewModel.Companion.UpdateUserResult.INVALID_FORM) {
                for(invalidField in it.invalidFields) {
                    when(invalidField.fieldName) {
                        "name" -> binding.profileFragmentName.error = invalidField.msg
                        "phone_number" -> binding.profileFragmentPhoneNumber.error = invalidField.msg
                        "general" -> Snackbar.make(
                            container!!,
                            invalidField.msg,
                            Snackbar.LENGTH_LONG
                        ).show();
                    }
                }
                return@observe
            }

            if (it is ProfileViewModel.Companion.UpdateUserResult.NETWORK_ERROR) {
                Snackbar.make(container!!, "No internet connection.", Snackbar.LENGTH_LONG).show();
                return@observe
            }

            if (it is ProfileViewModel.Companion.UpdateUserResult.SUCCESS) {
                Snackbar.make(container!!, "Your changes have been saved.", Snackbar.LENGTH_LONG).show()
                return@observe
            }
        })

        val toolbar = binding.topAppBar

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }

        return binding.root
    }

    override fun onSaveButtonClick(v: View) {
        viewModel.saveChanges()
    }

    private fun clearErrors() {
        binding.profileFragmentName.error = null
        binding.profileFragmentPhoneNumber.error = null
    }
}