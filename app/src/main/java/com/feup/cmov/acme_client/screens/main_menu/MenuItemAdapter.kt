package com.feup.cmov.acme_client.screens.main_menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.MenuItem

class MenuItemAdapter : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {

    var data = listOf<MenuItem>()
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
        private val name: TextView = itemView.findViewById(R.id.item_name)
        private val price: TextView = itemView.findViewById(R.id.item_price)
        private val category: TextView = itemView.findViewById(R.id.item_category)


        fun bind(item: MenuItem) {
            name.text = item.name
            price.text = item.price.toString() + " €" //dar fix a isto
            category.text = item.category
            //picture.src = item.pictureSrc ou algo assim
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.row, parent, false)

                return ViewHolder(view)
            }
        }
    }
}