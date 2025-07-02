package com.example.hatshop.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hatshop.R
import com.example.hatshop.database.DBHelper

class OrderDetailDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val orderId = arguments?.getInt("orderId") ?: return super.onCreateDialog(savedInstanceState)
        val dbHelper = DBHelper(requireContext())
        val details = dbHelper.getOrderDetailsDisplay(orderId)

        val msg = details.joinToString("\n") {
            "${it.productName} - SL: ${it.quantity} - ${it.price}đ"
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Chi tiết đơn hàng #$orderId")
            .setMessage(msg)
            .setPositiveButton("Đóng", null)
            .create()
    }

    companion object {
        fun newInstance(orderId: Int): OrderDetailDialogFragment {
            val f = OrderDetailDialogFragment()
            f.arguments = Bundle().apply { putInt("orderId", orderId) }
            return f
        }
    }
}