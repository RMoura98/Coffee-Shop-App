package com.feup.cmov.acme_client.screens.main_menu

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.databinding.FragmentMainMenuBinding
import com.google.android.material.badge.BadgeDrawable
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainMenuFragment : Fragment(), MainMenuHandler {

    private lateinit var myContext: FragmentActivity
    private val viewModel: MainMenuViewModel by viewModels()
    lateinit var binding: FragmentMainMenuBinding

    private var cartList = mutableMapOf<Long, Int>() // (ID, QUANTITY)
    private var totalCartItems: Int = 0


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

        binding.cartButton.visibility = if (totalCartItems > 0) View.VISIBLE else View.GONE

        val bottomNavigation = binding.bottomNavigation

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
        with(myContext.supportFragmentManager.beginTransaction()) {
            detach(fragment);
            attach(fragment);
            replace(R.id.content_frame, fragment)
            commit()
        }
    }

    fun addItemToCart(itemId: Long){
        // Add to map
        cartList[itemId] = cartList.getOrElse<Long, Int>(itemId, { 0 }) + 1
        Log.d("Added to cart ID: ", itemId.toString())

        // Total number of items
        totalCartItems++
        binding.cartButtonNumberItems.text = totalCartItems.toString()
        binding.cartButton.visibility = if (totalCartItems > 0) View.VISIBLE else View.GONE

        // Update cart on cartfragment

        Log.d("Cart List: ", cartList.toString())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("DEBGUG: onSIS", "mainMenu")
    }

    fun getMenuItemsLiveData(): LiveData<List<MenuItem>> {
        return viewModel.getMenuItems()
    }

    fun getCartItems(): List<MenuItem> {
        var menuItemList = viewModel.getMenuItems().value
        return menuItemList?.filter { it.id in cartList.keys }!!
    }

}