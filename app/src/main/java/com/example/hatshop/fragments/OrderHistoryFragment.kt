package com.example.hatshop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hatshop.R
import com.example.hatshop.database.DBHelper

class OrderHistoryFragment : Fragment(R.layout.fragment_order_history) {
    private lateinit var dbHelper: DBHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dbHelper = DBHelper(requireContext())
        val userId = arguments?.getInt("userId") ?: return

        val orderList = dbHelper.getOrdersByUserId(userId)
        val container = view.findViewById<LinearLayout>(R.id.orderListContainer)
        container.removeAllViews()

        orderList.forEach { order ->
            val tv = TextView(requireContext())
            tv.text = "Đơn hàng #${order.id} - ${order.orderDate} - ${order.totalAmount}đ"
            tv.setPadding(0, 16, 0, 16)
            tv.setOnClickListener {
                val dialog = OrderDetailDialogFragment.newInstance(order.id)
                dialog.show(parentFragmentManager, "OrderDetail")
            }
            container.addView(tv)
        }
    }

    companion object {
        fun newInstance(userId: Int): OrderHistoryFragment {
            val fragment = OrderHistoryFragment()
            fragment.arguments = Bundle().apply { putInt("userId", userId) }
            return fragment
        }
    }
}
