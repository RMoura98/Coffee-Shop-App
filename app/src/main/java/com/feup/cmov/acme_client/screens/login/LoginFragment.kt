package com.feup.cmov.acme_client.screens.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentLoginBinding
import com.feup.cmov.acme_client.forms.InvalidField
import com.feup.cmov.acme_client.screens.signup.SignupFragmentDirections
import com.feup.cmov.acme_client.screens.signup.SignupFragment_GeneratedInjector
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class LoginFragment : Fragment(), LoginHandler {

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login, container, false)

        // Setting binding params
        binding.viewModel = viewModel
        binding.handler = this

        // Watching for login result
        viewModel.getLoginResult().observe(viewLifecycleOwner, Observer observe@ { result ->
            // This IF fixes a bug: when going back to this fragment the view model not being cleared and so Toasts are so annoying.
            if(viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED)
                return@observe

            if (result === LoginViewModel.LoginResults.INVALID_FORM) {
                val invalidFields: ArrayList<InvalidField> = viewModel.getInvalidFields().value!!
                for(invalidField in invalidFields) {
                    when(invalidField.fieldName) {
                        "userName" -> binding.loginFragmentUsernameInput.error = invalidField.msg
                        "password" -> binding.loginFragmentPasswordInput.error = invalidField.msg
                        "general" -> Snackbar.make(container!!, invalidField.msg, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
            else if (result === LoginViewModel.LoginResults.NETWORK_ERROR) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show()
            }

            else if (result === LoginViewModel.LoginResults.SUCCESS) {
                loginSuccessful(container!!)
            }
        })

        return binding.root
    }

    override fun onLoginButtonClick(v: View) {
        viewModel.performLogin()
    }

    override fun onSignupButtonClick(v: View) {
        v.findNavController()
            .navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
    }

    fun loginSuccessful(v: View) {
        Snackbar.make(v, "Login is success :D.", Snackbar.LENGTH_LONG).show();
        v.findNavController()
            .navigate(LoginFragmentDirections.actionLoginFragmentToMainMenuFragment())
    }

}