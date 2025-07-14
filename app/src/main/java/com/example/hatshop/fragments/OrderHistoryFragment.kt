package com.example.hatshop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hatshop.R
import com.example.hatshop.database.DBHelper
import com.example.hatshop.adapters.OrderAdapter
import com.example.hatshop.models.Order

class OrderHistoryFragment : Fragment(R.layout.fragment_order_history) {
    private lateinit var dbHelper: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())

        recyclerView = view.findViewById(R.id.recyclerOrderHistory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val userId = arguments?.getInt("userId") ?: return
        val orderList = dbHelper.getOrdersByUserId(userId)

        orderAdapter = OrderAdapter(orderList) { order ->
            val dialog = OrderDetailDialogFragment.newInstance(order.id)
            dialog.show(parentFragmentManager, "OrderDetail")
        }
        recyclerView.adapter = orderAdapter
    }

    companion object {
        fun newInstance(userId: Int): OrderHistoryFragment {
            val fragment = OrderHistoryFragment()
            fragment.arguments = Bundle().apply { putInt("userId", userId) }
            return fragment
        }
    }
}
