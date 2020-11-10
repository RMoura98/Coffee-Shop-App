package com.feup.cmov.acme_client.screens.store

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.AcmeApplication
import com.squareup.picasso.Picasso


class MenuItemAdapter(private val storeHandler: StoreHandler) : RecyclerView.Adapter<MenuItemAdapter.ViewHolderBase>() {

    sealed class MenuItemData {
        data class MenuItemWrapper (val menuItem: MenuItem) : MenuItemData()
        data class Header (val category: String) : MenuItemData()
    }

    var data = listOf<MenuItemData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return when(data[position]) {
            is MenuItemData.MenuItemWrapper -> 0
            is MenuItemData.Header -> 1
        }
    }

    override fun onBindViewHolder(holder: ViewHolderBase, position: Int) {
        when (holder) {
            is ViewHolderProduct -> holder.bind((data[position] as MenuItemData.MenuItemWrapper).menuItem, storeHandler)
            is ViewHolderCategory -> holder.bind((data[position] as MenuItemData.Header).category)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBase {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            0 -> {
                ViewHolderProduct(layoutInflater
                    .inflate(R.layout.store_row, parent, false))
            }
            else -> {
                ViewHolderCategory(layoutInflater
                    .inflate(R.layout.category_row, parent, false))
            }
        }
    }

    open class ViewHolderBase constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ViewHolderProduct constructor(itemView: View) : ViewHolderBase(itemView) {
        private val name: TextView = itemView.findViewById(R.id.item_name)
        private val price: TextView = itemView.findViewById(R.id.item_price)
        private val imageView: ImageView =  itemView.findViewById(R.id.item_image)
        private val linearLayout: View = itemView.findViewById(R.id.store_row_layout)
        private val description: TextView = itemView.findViewById(R.id.description)

        private val assetPath: String = AcmeApplication.getAppContext().getString(R.string.serverURL) + "/assets/"
        private val priceStringFormat: String = AcmeApplication.getAppContext().getString(R.string.item_price)

        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: MenuItem, storeHandler: StoreHandler) {
            name.text = item.name
            price.text = String.format(priceStringFormat, item.price)
            description.text = item.description

            val imagePath = assetPath + item.imageName
            Picasso.get().load(imagePath).placeholder(R.drawable.coffee_trimmed).into(imageView)
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
    }

    class ViewHolderCategory constructor(itemView: View) : ViewHolderBase(itemView) {
        private val categoryName: TextView = itemView.findViewById(R.id.categoryName)

        fun bind(category: String) {
            categoryName.text = category
        }
    }

}