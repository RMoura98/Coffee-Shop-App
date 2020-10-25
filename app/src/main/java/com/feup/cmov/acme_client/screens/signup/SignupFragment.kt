package com.feup.cmov.acme_client.screens.signup

import android.graphics.Color
import android.os.Bundle
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

            if (result is SignupViewModel.Companion.SignupResult.INVALID_FORM) {
                for(invalidField in result.invalidFields) {
                    when(invalidField.fieldName) {
                        "name" -> binding.nameInput.error = invalidField.msg
                        "NIF" -> binding.nifInput.error = invalidField.msg
                        "card_number" -> binding.cardNumberInput.error = invalidField.msg
                        "card_cvc" -> binding.cardCVCInput.error = invalidField.msg
                        "card_expiration" -> binding.cardExpirationInput.error = invalidField.msg
                        "phone_number" -> binding.phoneInput.error = invalidField.msg
                        "username" -> binding.userNameInput.error = invalidField.msg
                        "password" -> binding.passwordInput.error = invalidField.msg
                        "general" -> Snackbar.make(container!!, invalidField.msg, Snackbar.LENGTH_LONG).show();
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
        Snackbar.make(v, "Register is success :D.", Snackbar.LENGTH_LONG).show();
        val bundle = bundleOf("userName" to user.userName)
        v.findNavController()
            .navigate(R.id.action_signupFragment_to_mainMenuFragment, bundle)
    }

    override fun onSubmitButtonClick(v: View) {
        viewModel.performSignup()
    }
}