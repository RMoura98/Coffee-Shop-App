package com.feup.cmov.acme_client.screens.store

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.databinding.FragmentStoreBinding
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreFragment() : Fragment(), StoreHandler {

    private val cartViewModel: CartViewModel by activityViewModels()
    lateinit var binding: FragmentStoreBinding
    private lateinit var adapter: MenuItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_store, container, false
        )

        cartViewModel.getMenuItems().observe(viewLifecycleOwner, Observer observe@{ menuItems: List<MenuItem> ->
            val itemsList = ArrayList<MenuItemAdapter.MenuItemData>()
            val sortedItems = menuItems.sortedWith(compareBy({ it.category }, { it.name }))
            var lastCategory = ""
            for(menuItem in sortedItems) {
                if(menuItem.category != lastCategory) {
                    itemsList.add(MenuItemAdapter.MenuItemData.Header(menuItem.category))
                    itemsList.add(MenuItemAdapter.MenuItemData.MenuItemWrapper(menuItem))
                    lastCategory = menuItem.category
                }
                else
                    itemsList.add(MenuItemAdapter.MenuItemData.MenuItemWrapper(menuItem))

            }

            adapter.data = itemsList
        });

        cartViewModel.getLoggedInUser().observe(viewLifecycleOwner, Observer observe@{ user ->
            binding.loggedInUserName.text = user?.name
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MenuItemAdapter(this)
        binding.mainMenuFragmentItemsList.adapter = adapter
    }

    override fun addToCartOnClick(item: MenuItem, x: Float, y: Float, drawable: Drawable) {
        // Log.e("StoreFragment X", x.toString())
        // Log.e("StoreFragment Y", y.toString())

        val imageView = ImageView(this.context)
        imageView.layoutParams = LinearLayout.LayoutParams(120, 120) // value is in pixels
        (imageView.layoutParams as LinearLayout.LayoutParams).setMargins(x.toInt() - 120 / 2, y.toInt() - 120, 0, 0);

        imageView.setImageDrawable(drawable)
        binding.newImageLayout.addView(imageView)

//        Log.e("height", binding.wrapperLayout.height.toString())
//        Log.e("y", y.toString())
//        Log.e("minus", (binding.wrapperLayout.height.toFloat() - y).toString())

        val howFarAway = (binding.wrapperLayout.height.toFloat() - y * 0.69) / (binding.wrapperLayout.height.toFloat())
        with(imageView.animate()){
            translationY(binding.wrapperLayout.height.toFloat() * 2)
            setDuration(1000)
        }

        GlobalScope.launch(Dispatchers.Main) {
            delay((1000 * howFarAway).toLong() - 150)
            cartViewModel.addItemToCart(item)
            delay(1000)
            binding.newImageLayout.removeView(imageView)
        }
    }

}