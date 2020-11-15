package com.feup.cmov.acme_terminal.screens.order_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_terminal.AcmeApplication
import com.feup.cmov.acme_terminal.R
import com.feup.cmov.acme_terminal.database.models.OrderWithItems

class OrderItemAdapter(private val ordersHistoryHandler: OrdersHistoryHandler) : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

    private var isFirstTimeShowing: Boolean = true

    var data = listOf<OrderWithItems>()
        set(value) {
            var sortedValue = value.sortedByDescending { it.order.order_sequential_id }
            if(field != sortedValue)
                notifyDataSetChanged()
            field = sortedValue
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: OrderWithItems = data[position]

        holder.bind(item, ordersHistoryHandler, shouldShowAnimation(position))
        if (isFirstTimeShowing) isFirstTimeShowing = false
    }

    private fun shouldShowAnimation(position: Int): Boolean {
        return position == 0 && ordersHistoryHandler.wasAnOrderAdded() && isFirstTimeShowing
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container: View = itemView.findViewById(R.id.order_row_template)
        private val mainText: TextView = itemView.findViewById(R.id.order_main_text)
        private val orderDateText: TextView = itemView.findViewById(R.id.order_date)
        private val orderItemDescription: TextView = itemView.findViewById(R.id.order_info)
        private val orderCompletedIcon: ImageView = itemView.findViewById(R.id.orderCompletedIcon)

        fun bind(item: OrderWithItems, ordersHistoryHandler: OrdersHistoryHandler, showAnimation: Boolean) {
            mainText.text = "Order #${item.order.order_sequential_id}"
            orderDateText.text = item.order.formatCompletedDate()

            val numberOfItems = item.getNumberOfItemsBought()
            val totalPrice = item.order.total
            orderItemDescription.text = "${numberOfItems} ${if(numberOfItems == 1L) "item" else "items"} | ${String.format("%.2f", totalPrice)} €"

            orderCompletedIcon.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
            orderCompletedIcon.setColorFilter(ContextCompat.getColor(AcmeApplication.getAppContext(), R.color.green_800), android.graphics.PorterDuff.Mode.SRC_IN)

            container.setOnClickListener {
                ordersHistoryHandler.viewOrder(itemView, item)
            }

            if (showAnimation) {
                itemView.translationY = -250f
                with(itemView.animate()){
                    translationY(0f)
                    setDuration(250)
                }
            }
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.order_row, parent, false)

                return ViewHolder(view)
            }
        }
    }

}