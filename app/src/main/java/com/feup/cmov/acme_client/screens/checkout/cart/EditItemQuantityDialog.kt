package com.feup.cmov.acme_client.screens.checkout.cart

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.MainActivity
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.screens.checkout.CartViewModel
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso

class EditItemQuantityDialog(): DialogFragment() {
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var inflaterView: View
    private lateinit var cartItem: CartViewModel.CartItem
    private var originalQuantity: Int = 0

    private lateinit var updateCartButton: MaterialButton
    private lateinit var updateCartButtonString: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            inflaterView = inflater.inflate(R.layout.custom_dialog_fragment, null)

            setProductInformation(inflaterView)
            setQuantitySection(inflaterView)
            setButtons(inflaterView)

            builder.setView(inflaterView)
            val dialog: Dialog = builder.create()
            dialog.setCanceledOnTouchOutside(true)
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setButtons(view: View) {
        val removeAllButton: TextView = view.findViewById(R.id.remove_all_button)
        updateCartButton = view.findViewById(R.id.update_cart_button)
        updateCartButtonString = AcmeApplication.getAppContext().getString(R.string.update_cart_value)

        updateUpdateButton(view)

        updateCartButton.setOnClickListener {
            cartViewModel.updateCartItem(cartItem, originalQuantity)
            dismiss()
        }
        removeAllButton.setOnClickListener {
            cartItem.quantity = 0
            cartViewModel.updateCartItem(cartItem, originalQuantity)
            dismiss()
        }
    }

    private fun updateUpdateButton(view: View) {
        var totalPrice =  cartItem.quantity * cartItem.item.price
        updateCartButton.text = String.format(updateCartButtonString, totalPrice)
    }

    private fun setQuantitySection(view: View) {

        val numberItems: TextView =  view.findViewById(R.id.cart_button_number_items)
        val minusButton: ImageView =  view.findViewById(R.id.minus_button)
        val plusButton: ImageView = view.findViewById(R.id.plus_button)

        numberItems.text = cartItem.quantity.toString()

        minusButton.setOnClickListener {
            if (cartItem.quantity == 0) return@setOnClickListener
            cartItem.quantity--
            numberItems.text = cartItem.quantity.toString()
            updateUpdateButton(view)
        }
        plusButton.setOnClickListener {
            cartItem.quantity++
            numberItems.text = cartItem.quantity.toString()
            updateUpdateButton(view)
        }
    }

    private fun setProductInformation(view: View) {
        val name: TextView = view.findViewById(R.id.item_name)
        val price: TextView = view.findViewById(R.id.item_price)
        val category: TextView = view.findViewById(R.id.item_category)
        val imageView: ImageView =  view.findViewById(R.id.item_image)

        val priceStringFormat = AcmeApplication.getAppContext().getString(R.string.item_price)
        val assetPath: String = AcmeApplication.getAppContext().getString(R.string.serverURL) + "/assets/"
        val imagePath = assetPath + cartItem.item.imageName

        name.text = cartItem.item.name
        price.text = String.format(priceStringFormat, cartItem.item.price)
        category.text = cartItem.item.category
        Picasso.get().load(imagePath).into(imageView);
    }

    fun show(cartItem: CartViewModel.CartItem) {
        this.cartItem = cartItem
        this.originalQuantity = cartItem.quantity
        this.show(MainActivity.getFragmentManager(), "Custom Dialog")
    }

}