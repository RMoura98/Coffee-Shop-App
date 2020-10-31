package com.feup.cmov.acme_client.screens.vouchers

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.MenuItem
import com.feup.cmov.acme_client.database.models.Voucher
import com.squareup.picasso.Picasso

class VoucherAdapter: RecyclerView.Adapter<VoucherAdapter.ViewHolder>() {

    var data = listOf<Voucher>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = data[position]
        holder.bind(voucher)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val voucherType: TextView = itemView.findViewById(R.id.voucher_type)
        private val used: TextView = itemView.findViewById(R.id.used)
        private val imageView: ImageView =  itemView.findViewById(R.id.voucher_image)

        fun bind(voucher: Voucher) {
            voucherType.text = when (voucher.voucherType) {
                "discount" -> {
                    imageView.setImageResource(R.drawable.discount)
                    itemView.resources.getString(R.string.discount_5)
                }
                "free_coffee" ->  {
                    imageView.setImageResource(R.drawable.coffee_trimmed)
                    itemView.resources.getString(R.string.free_coffee)
                }
                else -> ""
            }

            used.text = when (voucher.used) {
                true -> {
                    used.setTextColor(Color.RED)
                    "Used"
                }
                false -> {
                    used.setTextColor(Color.GREEN)
                    "Unused"
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.voucher_row, parent, false)

                return ViewHolder(view)
            }
        }
    }
}