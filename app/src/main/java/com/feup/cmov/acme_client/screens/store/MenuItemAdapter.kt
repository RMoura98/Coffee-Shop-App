package com.feup.cmov.acme_client.screens.store

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.MenuItem
import com.squareup.picasso.Picasso

class MenuItemAdapter(private val storeHandler: StoreHandler) : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {

    var data = listOf<MenuItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, storeHandler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.item_name)
        private val price: TextView = itemView.findViewById(R.id.item_price)
        private val category: TextView = itemView.findViewById(R.id.item_category)
        private val imageView: ImageView =  itemView.findViewById(R.id.item_image)
        private val categoryIcon: ImageView = itemView.findViewById(R.id.item_category_icon)
        private val linearLayout: LinearLayout = itemView.findViewById(R.id.store_row_layout)

        private val assetPath: String = AcmeApplication.getAppContext().getString(R.string.serverURL) + "/assets/"
        private val priceStringFormat: String = AcmeApplication.getAppContext().getString(R.string.item_price)
        private val foodIcon: Drawable? = AcmeApplication.getAppContext().getDrawable(R.drawable.ic_restaurant_black_18dp)
        private val drinkIcon: Drawable? = AcmeApplication.getAppContext().getDrawable(R.drawable.ic_wine_bar_black_18dp)



        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: MenuItem, storeHandler: StoreHandler) {
            name.text = item.name
            price.text = String.format(priceStringFormat, item.price)
            category.text = item.category

            when (item.category) {
                "Food" -> categoryIcon.setImageDrawable(foodIcon)
                "Drinks" -> categoryIcon.setImageDrawable(drinkIcon)
            }

            val imagePath = assetPath + item.imageName
            Picasso.get().load(imagePath).into(imageView);

            var lastX = 0f
            var lastY = 0f
            linearLayout.setOnClickListener { storeHandler.addToCartOnClick(item, lastX, lastY, imageView.drawable) }
            linearLayout.setOnTouchListener { view: View, motionEvent: MotionEvent ->
                if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN) {
                    val viewCoords = IntArray(2)
                    view.getLocationOnScreen(viewCoords)
                    lastX = viewCoords[0].toFloat() + motionEvent.getX()
                    lastY = viewCoords[1].toFloat() + motionEvent.getY()
                }
                false
            }

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