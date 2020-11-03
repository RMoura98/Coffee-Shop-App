package com.feup.cmov.acme_client.screens.main_menu

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.databinding.FragmentMainMenuBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainMenuFragment : Fragment(), MainMenuHandler {

    private lateinit var myContext: FragmentActivity
    private val viewModel: MainMenuViewModel by viewModels()
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

        val bottomNavigation = binding.bottomNavigation

        var badge = bottomNavigation.getOrCreateBadge(R.id.cartAction)
        badge.isVisible = true
        badge.number = 12

        // Create the fragments
        makeCurrentFragment(viewModel.getCurrentFragment())

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val menuItem = bottomNavigation.menu.findItem(item.itemId)
            if(menuItem.itemId != viewModel.getCurrentAction()) {
                menuItem.isChecked = true
                viewModel.setCurrentAction(menuItem.itemId)
                makeCurrentFragment(viewModel.getCurrentFragment())
            }
            false
        }

        return binding.root
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        myContext.supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }
}