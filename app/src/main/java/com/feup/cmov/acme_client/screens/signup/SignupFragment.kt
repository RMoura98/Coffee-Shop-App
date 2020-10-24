package com.feup.cmov.acme_client.screens.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentLoginBinding
import com.feup.cmov.acme_client.databinding.FragmentSignupBinding
import com.feup.cmov.acme_client.screens.login.LoginViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        return binding.root
    }

    override fun onSubmitButtonClick(v: View) {
        viewModel.performSignup()
    }
}