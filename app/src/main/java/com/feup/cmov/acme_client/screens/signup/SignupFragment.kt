package com.feup.cmov.acme_client.screens.signup

import android.R.attr.editable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentSignupBinding
import com.feup.cmov.acme_client.forms.InvalidField
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
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
        viewModel.getSignupResult().observe(viewLifecycleOwner, Observer observe@{ result ->
            // This IF fixes a bug: when going back to this fragment the view model not being cleared and so Toasts are so annoying.
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED)
                return@observe

            if (result === SignupViewModel.SignupResult.INVALID_FORM) {
                val invalidFields: ArrayList<InvalidField> = viewModel.getInvalidFields().value!!
                for (invalidField in invalidFields) {
                    when (invalidField.fieldName) {
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

            if (result === SignupViewModel.SignupResult.NETWORK_ERROR) {
                Snackbar.make(container!!, "No internet connection.", Snackbar.LENGTH_LONG).show();
                return@observe
            }

            if (result === SignupViewModel.SignupResult.SUCCESS) {
                signupSuccessful(container!!)
                return@observe
            }
        })

        return binding.root
    }

    fun signupSuccessful(v: View) {
        Snackbar.make(v, "Register is success :D.", Snackbar.LENGTH_LONG).show();
        v.findNavController()
            .navigate(SignupFragmentDirections.actionSignupFragmentToMainMenuFragment())
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

}