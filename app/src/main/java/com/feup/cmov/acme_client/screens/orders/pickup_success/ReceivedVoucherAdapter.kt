package com.feup.cmov.acme_client.screens.orders.pickup_success

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.Voucher
import com.feup.cmov.acme_client.database.models.composed_models.VoucherWithOrder

class ReceivedVoucherAdapter: RecyclerView.Adapter<ReceivedVoucherAdapter.ViewHolder>() {

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
        private val voucherCaption: TextView = itemView.findViewById(R.id.voucher_caption)
        private val usedCaption: TextView = itemView.findViewById(R.id.used_caption)
        private val usedBar: View = itemView.findViewById(R.id.used_bar)
        private val imageView: ImageView =  itemView.findViewById(R.id.voucher_image)

        fun bind(voucher: Voucher) {
            when (voucher.voucherType) {
                "discount" -> {
                    imageView.setImageResource(R.drawable.voucher_discount)
                    voucherType.text = itemView.resources.getString(R.string.discount)
                    voucherCaption.text = itemView.resources.getString(R.string.discount_5)
                }
                "free_coffee" ->  {
                    imageView.setImageResource(R.drawable.voucher_free_item)
                    voucherType.text = itemView.resources.getString(R.string.free_item)
                    voucherCaption.text = itemView.resources.getString(R.string.free_item_coffee)
                }
            }

            usedBar.visibility = View.GONE
            usedCaption.visibility = View.GONE
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