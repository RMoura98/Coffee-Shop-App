package com.feup.cmov.acme_client.screens.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.feup.cmov.acme_client.AcmeApplication
import com.feup.cmov.acme_client.R
import com.feup.cmov.acme_client.database.models.composed_models.OrderWithItems
import java.math.BigInteger
import java.security.MessageDigest
import java.text.DateFormat
import java.text.SimpleDateFormat

class OrderItemAdapter(private val ordersHistoryHandler: OrdersHistoryHandler) : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

    enum class SHOWING {
        COMPLETED_ORDERS, ONGOING_ORDERS
    }

    var showing = SHOWING.COMPLETED_ORDERS
        set(value) {
            if(field != value)
                notifyDataSetChanged()
            field = value
        }

    var completed_orders = listOf<OrderWithItems>()
    var ongoing_orders = listOf<OrderWithItems>()

    var data = listOf<OrderWithItems>()
        set(value) {
            field = value
            completed_orders = data.filter { item -> item.order.completed }
            ongoing_orders = data.filter { item -> !item.order.completed }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        if(showing == SHOWING.COMPLETED_ORDERS)
            return completed_orders.size
        else
            return ongoing_orders.size
    }

    override fun onBindViewHolder(holder: OrderItemAdapter.ViewHolder, position: Int) {
        val item: OrderWithItems
        if(showing == SHOWING.COMPLETED_ORDERS)
            item = completed_orders[position]
        else
            item = ongoing_orders[position]
        holder.bind(item, ordersHistoryHandler)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mainText: TextView = itemView.findViewById(R.id.order_main_text)
        private val orderDateText: TextView = itemView.findViewById(R.id.order_date)
        private val orderItemDescription: TextView = itemView.findViewById(R.id.order_info)
        private val orderCompletedIcon: ImageView = itemView.findViewById(R.id.orderCompletedIcon)

        fun bind(item: OrderWithItems, ordersHistoryHandler: OrdersHistoryHandler) {
            if(item.order.completed)
                mainText.text = "Order #${item.order.order_sequential_id}"
            else
                mainText.text = "Order Pending"
            orderDateText.text = item.formatCompletedDate()

            val numberOfItems = item.getNumberOfItemsBought()
            val totalPrice = item.getTotalPrice()
            orderItemDescription.text = "${numberOfItems} ${if(numberOfItems == 1L) "item" else "items"} | ${String.format("%.2f", totalPrice)} €"

            if(item.order.completed) {
                orderCompletedIcon.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                orderCompletedIcon.setColorFilter(ContextCompat.getColor(AcmeApplication.getAppContext(), R.color.green_800), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                orderCompletedIcon.setImageResource(R.drawable.ic_outline_play_circle_outline_24)
                orderCompletedIcon.setColorFilter(ContextCompat.getColor(AcmeApplication.getAppContext(), R.color.orange_800), android.graphics.PorterDuff.Mode.SRC_IN);
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