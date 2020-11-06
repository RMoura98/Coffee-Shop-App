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
import com.feup.cmov.acme_client.screens.main_menu.CartViewModel
import org.w3c.dom.Text


class VoucherSelectionAdapter(private val cartViewModel: CartViewModel, private val selectionHandler: VoucherSelectionHandler) :
    RecyclerView.Adapter<VoucherSelectionAdapter.ViewHolder>() {

    var data = listOf<Voucher>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = data[position]
        if(voucher.voucherType == "free_coffee" && !cartViewModel.isCoffeeInTheCart()){
            holder.bind(voucher, selectionHandler, cartViewModel.isVoucherSelected(voucher), "Not applicable.")
        }
        else if(voucher.voucherType == "free_coffee" && cartViewModel.isAnyCoffeVoucherSelected() && !cartViewModel.isVoucherSelected(voucher)){
            holder.bind(voucher, selectionHandler, cartViewModel.isVoucherSelected(voucher), "Maximum of 1.")
        }
        else {
            holder.bind(voucher, selectionHandler, cartViewModel.isVoucherSelected(voucher), null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val voucherType: TextView = itemView.findViewById(R.id.voucher_type)
        private val voucherCaption: TextView = itemView.findViewById(R.id.voucher_caption)
        private val imageView: ImageView =  itemView.findViewById(R.id.voucher_image)
        private val layout: LinearLayout = itemView.findViewById(R.id.wrapperLayout)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val checkBoxWrapper: LinearLayout = itemView.findViewById(R.id.checkBoxWrapper)
        private val canUseCaption: TextView = itemView.findViewById(R.id.can_use_caption)
        private val canUseWrapper: LinearLayout = itemView.findViewById(R.id.can_use_wrapper)
        private val canUseBar: View = itemView.findViewById(R.id.can_use_bar)

        fun bind(voucher: Voucher, selectionHandler: VoucherSelectionHandler, isChecked: Boolean, canBeUsedText: String?) {
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

            checkBox.setOnCheckedChangeListener (null);
            checkBox.isChecked = isChecked
            checkBox.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                selectionHandler.onCheckboxTick(voucher, isChecked)
            }

            if(canBeUsedText == null) {
                canUseWrapper.visibility = View.GONE
                checkBoxWrapper.visibility = View.VISIBLE
            } else {
                canUseWrapper.visibility = View.VISIBLE
                checkBoxWrapper.visibility = View.GONE
                canUseCaption.text = canBeUsedText
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