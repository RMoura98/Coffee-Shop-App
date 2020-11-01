package com.feup.cmov.acme_client.screens.main_menu.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.MenuItem
import com.squareup.picasso.Picasso

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
        private val imageView: ImageView =  itemView.findViewById(R.id.item_image)
        private val assetPath : String = AcmeApplication.getAppContext().getString(R.string.serverURL) + "/assets/"
        private val priceStringFormat : String = AcmeApplication.getAppContext().getString(R.string.item_price)

        fun bind(item: MenuItem) {
            name.text = item.name
            price.text = String.format(priceStringFormat, item.price)
            category.text = item.category

            val imagePath = assetPath + item.imageName
            Picasso.get().load(imagePath).into(imageView);
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.store_row, parent, false)

                return ViewHolder(view)
            }
        }
    }
}