package com.thedramaticcolumnist.appdistributor.ui.revenue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.myOrder
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mLog
import com.thedramaticcolumnist.appdistributor.databinding.MyRevenueBinding
import com.thedramaticcolumnist.appdistributor.databinding.OrderItemLayoutBinding
import com.thedramaticcolumnist.appdistributor.mViewHolder.OrderViewHolder
import com.thedramaticcolumnist.appdistributor.models.ProductModel


class MyRevenue : Fragment() {

    private var _binding: MyRevenueBinding? = null
    private val bind get() = _binding!!
    lateinit var recyclerAdapter: FirebaseRecyclerAdapter<ProductModel, OrderViewHolder>
    private var monthRevenue: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MyRevenueBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        showRevenue()
    }

    private fun showRevenue() {
        myOrder.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun initAllComponent() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}