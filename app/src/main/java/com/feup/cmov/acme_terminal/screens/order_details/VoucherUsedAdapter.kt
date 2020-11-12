package com.feup.cmov.acme_terminal.screens.order_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_terminal.AcmeApplication
import com.feup.cmov.acme_terminal.R
import com.feup.cmov.acme_terminal.database.models.Voucher


class VoucherUsedAdapter: RecyclerView.Adapter<VoucherUsedAdapter.ViewHolder>() {

    data class VoucherWithSavings(val voucher: Voucher, val savings: Float)

    var data = listOf<VoucherWithSavings>()
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
        private val imageView: ImageView =  itemView.findViewById(R.id.voucher_image)
        private val savingsCaption: TextView = itemView.findViewById(R.id.savings_caption)

        fun bind(voucherWithSavings: VoucherWithSavings) {
            when (voucherWithSavings.voucher.voucherType) {
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

            val priceStringFormat: String = AcmeApplication.getAppContext().getString(R.string.cart_price)
            savingsCaption.text = "- " + String.format(priceStringFormat, voucherWithSavings.savings)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.voucher_used_row, parent, false)

                return ViewHolder(view)
            }
        }
    }
}