package com.feup.cmov.acme_client.screens.signup

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.User
import com.feup.cmov.acme_client.databinding.FragmentLoginBinding
import com.feup.cmov.acme_client.databinding.FragmentSignupBinding
import com.feup.cmov.acme_client.forms.InvalidField
import com.feup.cmov.acme_client.screens.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SignupFragment : Fragment(), SignupHandler {
    private val viewModel: SignupViewModel by viewModels()
    lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signup, container, false
        )

        // Setting binding params
        binding.viewModel = viewModel
        binding.handler = this

        // Watching for login result
        viewModel.getSignupResult().observe(viewLifecycleOwner, Observer observe@ { result ->
            // This IF fixes a bug: when going back to this fragment the view model not being cleared and so Toasts are so annoying.
            if(viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED)
                return@observe

            clearErrors()

            if (result is SignupViewModel.Companion.SignupResult.INVALID_FORM) {
                for(invalidField in result.invalidFields) {
                    when(invalidField.fieldName) {
                        "name" -> binding.signupFragmentNameInput.error = invalidField.msg
                        "NIF" -> binding.signupFragmentNifInput.error = invalidField.msg
                        "card_number" -> binding.signupFragmentCardNumberInput.error = invalidField.msg
                        "card_cvc" -> binding.signupFragmentCardCVCInput.error = invalidField.msg
                        "card_expiration" -> binding.signupFragmentCardExpirationInput.error = invalidField.msg
                        "phone_number" -> binding.signupFragmentPhoneInput.error = invalidField.msg
                        "username" -> binding.signupFragmentUserNameInput.error = invalidField.msg
                        "password" -> binding.signupFragmentPasswordInput.error = invalidField.msg
                        "general" -> Snackbar.make(
                            container!!,
                            invalidField.msg,
                            Snackbar.LENGTH_LONG
                        ).show();
                    }
                }
                return@observe
            }

            if (result is SignupViewModel.Companion.SignupResult.NETWORK_ERROR) {
                Snackbar.make(container!!, "No internet connection.", Snackbar.LENGTH_LONG).show();
                return@observe
            }

            if (result is SignupViewModel.Companion.SignupResult.SUCCESS) {
                signupSuccessful(container!!, result.user)
                return@observe
            }
        })

        return binding.root
    }

    fun signupSuccessful(v: View, user: User) {
        //Snackbar.make(v, "Register is success :D.", Snackbar.LENGTH_LONG).show();
        v.findNavController()
            .navigate(R.id.action_signupFragment_to_mainMenuFragment)
    }

    override fun onSubmitButtonClick(v: View) {
        viewModel.performSignup()
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
        binding.signupFragmentNameInput.error = null
        binding.signupFragmentNifInput.error = null
        binding.signupFragmentCardNumberInput.error = null
        binding.signupFragmentCardCVCInput.error = null
        binding.signupFragmentCardExpirationInput.error = null
        binding.signupFragmentPhoneInput.error = null
        binding.signupFragmentUserNameInput.error = null
        binding.signupFragmentPasswordInput.error = null
    }
}