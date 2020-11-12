package com.feup.cmov.acme_terminal.screens.order_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_terminal.R
import com.feup.cmov.acme_terminal.database.models.MenuItem

class CartItemAdapter() : RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {
    data class CartItem(val item: MenuItem, var quantity: Int = 1)

    var data = listOf<CartItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.cart_item_name)
        private val price: TextView = itemView.findViewById(R.id.cart_item_price)
        private val quantity: TextView = itemView.findViewById(R.id.cart_item_quantity)

        private val priceStringFormat: String = com.feup.cmov.acme_terminal.AcmeApplication.getAppContext().getString(R.string.cart_price)


        fun bind(cartItem: CartItem) {
            name.text = cartItem.item.name
            price.text = String.format(priceStringFormat, cartItem.item.price)
            quantity.text = cartItem.quantity.toString()
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