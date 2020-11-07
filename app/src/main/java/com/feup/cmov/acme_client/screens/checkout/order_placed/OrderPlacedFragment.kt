package com.feup.cmov.acme_client.screens.checkout.order_placed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentOrderPlacedBinding
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import com.feup.cmov.acme_client.screens.main_menu.MainMenuViewModel
import com.feup.cmov.acme_client.screens.orders.OrdersHistoryFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OrderPlacedFragment : Fragment(), OrderPlacedHandler {

    private val cartViewModel : CartViewModel by activityViewModels()
    private val mainMenuViewModel: MainMenuViewModel by activityViewModels()
    lateinit var binding: FragmentOrderPlacedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_placed, container, false
        )

        // Setting binding params
        binding.handler = this
        binding.viewModel = cartViewModel

        val progresBar = binding.progressBar

        GlobalScope.launch {
            val animationTime = 3000 // milliseconds
            for (i in 1..100) {
                delay( (animationTime / 100).toLong() )
                progresBar.progress = i
            }
            cartViewModel.clearViewModel()
            mainMenuViewModel.setCurrentAction(R.id.historyAction)
            OrdersHistoryFragment.nextTabIndex = 1
            container!!.findNavController()
                .navigate(R.id.action_orderPlacedFragment_to_viewOrderFragment)
        }

        //MainMenuViewModel by activityViewModels()

        return binding.root
    }
}