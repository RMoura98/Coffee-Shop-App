package com.feup.cmov.acme_client.screens.main_menu

import android.R.attr.paddingBottom
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentMainMenuBinding
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import com.feup.cmov.acme_client.utils.Measurements
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainMenuFragment : Fragment(), MainMenuHandler {

    private lateinit var myContext: FragmentActivity
    private val viewModel: MainMenuViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    lateinit var binding: FragmentMainMenuBinding

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        myContext = activity as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main_menu, container, false
        )

        binding.viewModel = viewModel
        binding.cartViewModel = cartViewModel
        binding.handler = this

        cartViewModel.getTotalCartItems().observe(viewLifecycleOwner, Observer observe@{ totalCartItems ->
            if(totalCartItems >= 1) {
                with(binding.cartButton.animate()){
                    translationY(0f)
                    setDuration(if(!hasShownCartAnimation) 300 else 0)
                }
                val animation = ValueAnimator.ofInt(Measurements.convertDptoPx(55L).toInt(), Measurements.convertDptoPx(55L * 2).toInt())
                animation.duration = if(!hasShownCartAnimation) 300 else 0
                animation.addUpdateListener { valueAnimator ->
                    binding.contentFrame.setPadding(
                        0,
                        0,
                        0,
                        valueAnimator.animatedValue.toString().toInt()
                    )
                }
                animation.start()
                hasShownCartAnimation = true

            }
            binding.cartButtonNumberItems.text = totalCartItems.toString()
        })

        val priceStringFormat: String = AcmeApplication.getAppContext().getString(R.string.cart_price)
        cartViewModel.getSubtotalCartPrice().observe(viewLifecycleOwner, Observer observe@{ totalCartPrice ->
            binding.cartButtonTotalPrice.text = String.format(priceStringFormat, totalCartPrice)
        })

        return binding.root
    }

    private fun makeCurrentFragment(fragment: Fragment, refresh: Boolean) {
        with(childFragmentManager.beginTransaction()) {
            if(refresh && !hasRefreshed) {
                detach(fragment)
                attach(fragment)
                hasRefreshed = false
            }
            replace(R.id.content_frame, fragment)
            commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create the fragments
        val bottomNavigation = binding.bottomNavigation
        makeCurrentFragment(viewModel.getCurrentFragment(), true)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val menuItem = bottomNavigation.menu.findItem(item.itemId)
            if(menuItem.itemId != viewModel.getCurrentAction()) {
                menuItem.isChecked = true
                viewModel.setCurrentAction(menuItem.itemId)
                makeCurrentFragment(viewModel.getCurrentFragment(), false)
            }
            false
        }
    }

    override fun onShowCartButtonClick(v: View) {
        v.findNavController()
            .navigate(R.id.action_mainMenuFragment_to_cartFragment)
    }

    companion object {
        var hasShownCartAnimation = false
        var hasRefreshed = false
    }
}