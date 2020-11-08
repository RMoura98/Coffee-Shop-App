package com.feup.cmov.acme_client.screens.checkout.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.screens.checkout.CartViewModel

class CartItemAdapter() : RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

    private val dialog = EditItemQuantityDialog()

    var data = listOf<CartViewModel.CartItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, dialog)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.cart_item_name)
        private val price: TextView = itemView.findViewById(R.id.cart_item_price)
        private val quantity: TextView = itemView.findViewById(R.id.cart_item_quantity)

        private val priceStringFormat: String = AcmeApplication.getAppContext().getString(R.string.cart_price)


        fun bind(cartItem: CartViewModel.CartItem, dialog: EditItemQuantityDialog) {
            name.text = cartItem.item.name
            price.text = String.format(priceStringFormat, cartItem.item.price)
            quantity.text = cartItem.quantity.toString()

            itemView.setOnClickListener {
                dialog.show(cartItem)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.cart_row, parent, false)

                return ViewHolder(view)
            }
        }
    }

}