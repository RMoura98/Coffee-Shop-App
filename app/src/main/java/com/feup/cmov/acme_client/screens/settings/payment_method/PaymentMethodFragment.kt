package com.feup.cmov.acme_client.screens.settings.payment_method

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentPaymentMethodBinding
import com.feup.cmov.acme_client.databinding.FragmentProfileBinding
import com.feup.cmov.acme_client.screens.signup.SignupViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentMethodFragment : Fragment(), PaymentMethodHandler {

    private val viewModel : PaymentMethodViewModel by viewModels()
    lateinit var binding: FragmentPaymentMethodBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment_method, container, false
        )

        // Setting binding params
        binding.viewModel = viewModel
        binding.handler = this

        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            binding.invalidateAll()
        })

        viewModel.getUpdateUserResult().observe(viewLifecycleOwner, Observer observe@ {
            clearErrors()

            if (it is PaymentMethodViewModel.Companion.UpdateUserResult.INVALID_FORM) {
                for(invalidField in it.invalidFields) {
                    when(invalidField.fieldName) {
                        "NIF" -> binding.profileFragmentNif.error = invalidField.msg
                        "card_number" -> binding.profileFragmentCardNumber.error = invalidField.msg
                        "card_cvc" -> binding.profileFragmentCardCVC.error = invalidField.msg
                        "card_expiration" -> binding.profileFragmentCardExpiration.error = invalidField.msg
                        "general" -> Snackbar.make(
                            container!!,
                            invalidField.msg,
                            Snackbar.LENGTH_LONG
                        ).show();
                    }
                }
                return@observe
            }

            if (it is PaymentMethodViewModel.Companion.UpdateUserResult.NETWORK_ERROR) {
                Snackbar.make(container!!, "No internet connection.", Snackbar.LENGTH_LONG).show();
                return@observe
            }

            if (it is PaymentMethodViewModel.Companion.UpdateUserResult.SUCCESS) {
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

    override fun afterTextChangedCardNumber(s: Editable) {
        val space = ' '

        // Remove spacing char
        if (s.isNotEmpty() && s.length % 5 === 0) {
            val c: Char = s[s.length - 1]
            if (space == c) {
                s.delete(s.length - 1, s.length)
            }
        }

        // Insert char where needed.
        if (s.isNotEmpty() && s.length % 5 === 0) {
            val c: Char = s[s.length - 1]
            // Only if its a digit where there should be a space we insert a space
            if (Character.isDigit(c) && TextUtils.split(
                    s.toString(),
                    space.toString()
                ).count() <= 3
            ) {
                s.insert(s.length - 1, space.toString())
            }
        }
    }

    override fun afterTextChangedCardExpiration(s: Editable) {
        if (s.isNotEmpty() && s.length % 3 === 0) {
            val c: Char = s[s.length - 1]
            if ('/' == c) {
                s.delete(s.length - 1, s.length)
            }
        }
        if (s.isNotEmpty() && s.length % 3 === 0) {
            val c: Char = s[s.length - 1]
            if (Character.isDigit(c) && TextUtils.split(s.toString(), "/").count() <= 2) {
                s.insert(s.length - 1, "/")
            }
        }
    }

    private fun clearErrors() {
        binding.profileFragmentNif.error = null
        binding.profileFragmentCardNumber.error = null
        binding.profileFragmentCardCVC.error = null
        binding.profileFragmentCardExpiration.error = null
    }
}