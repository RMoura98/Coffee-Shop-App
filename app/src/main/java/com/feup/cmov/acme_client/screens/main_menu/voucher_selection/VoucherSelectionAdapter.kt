package com.feup.cmov.acme_client.screens.main_menu.voucher_selection

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.Voucher


class VoucherSelectionAdapter(private val selectionHandler: VoucherSelectionHandler) :
    RecyclerView.Adapter<VoucherSelectionAdapter.ViewHolder>() {

    var data = listOf<Voucher>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = data[position]
        holder.bind(voucher, selectionHandler)
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
        private val layout: LinearLayout = itemView.findViewById(R.id.wrapperLayout)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)

        fun bind(voucher: Voucher, selectionHandler: VoucherSelectionHandler) {
            when (voucher.voucherType) {
                "discount" -> {
                    imageView.setImageResource(R.drawable.voucher_discount)
                    voucherType.text = itemView.resources.getString(R.string.discount)
                    voucherCaption.text = itemView.resources.getString(R.string.discount_5)
                }
                "free_coffee" ->  {
                    imageView.setImageResource(R.drawable.voucher_free_item)
                    voucherType.text = itemView.resources.getString(R.string.free_item)
                    voucherCaption.text = itemView.resources.getString(R.string.free_item_coffe)
                }
            }

            when (voucher.used) {
                true -> {
                    usedBar.setBackgroundColor(getColor(AcmeApplication.getAppContext(), R.color.red_500))
                    usedCaption.text = "Used on 05-10-2020"
                }
                false -> {
                    usedBar.setBackgroundColor(getColor(AcmeApplication.getAppContext(), R.color.green_500))
                    usedCaption.text = "No expiration date"
                }
            }

            checkBox.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                selectionHandler.onCheckboxTick(voucher, isChecked)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.voucher_select_row, parent, false)

                return ViewHolder(view)
            }
        }
    }
}