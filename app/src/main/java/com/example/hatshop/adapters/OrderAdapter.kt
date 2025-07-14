package com.example.hatshop.adapters
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.hatshop.models.Order
import android.widget.TextView
import com.example.hatshop.R
import android.view.ViewGroup
import android.view.LayoutInflater

class OrderAdapter(
    private val orders: List<Order>,
    private val onItemClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        private val tvOrderDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        private val tvOrderAmount: TextView = itemView.findViewById(R.id.tvOrderAmount)

        fun bind(order: Order) {
            tvOrderId.text = "Đơn hàng #${order.id}"
            tvOrderDate.text = "Ngày: ${order.orderDate}"
            tvOrderAmount.text = "Tổng tiền: ${order.totalAmount}đ"
            itemView.setOnClickListener {
                onItemClick(order)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }
}
