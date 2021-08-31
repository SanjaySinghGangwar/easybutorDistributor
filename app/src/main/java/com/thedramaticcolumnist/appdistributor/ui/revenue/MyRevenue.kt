package com.thedramaticcolumnist.appdistributor.ui.revenue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.myOrder
import com.thedramaticcolumnist.appdistributor.Utils.mUtils
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.formatDate
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.getCurrentMonthStartEndDate
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mLog
import com.thedramaticcolumnist.appdistributor.databinding.MyRevenueBinding
import com.thedramaticcolumnist.appdistributor.mViewHolder.OrderViewHolder
import com.thedramaticcolumnist.appdistributor.models.OrderDetailsModel
import com.thedramaticcolumnist.appdistributor.models.OrderDetailsWithID
import com.thedramaticcolumnist.appdistributor.models.ProductModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MyRevenue : Fragment() {

    private var _binding: MyRevenueBinding? = null
    private val bind get() = _binding!!
    lateinit var recyclerAdapter: FirebaseRecyclerAdapter<ProductModel, OrderViewHolder>


    private var tempList: ArrayList<OrderDetailsWithID> = ArrayList()
    private var orderList: ArrayList<OrderDetailsWithID> = ArrayList()
    private var monthOrderList: ArrayList<OrderDetailsWithID> = ArrayList()

    private var monthRevenue: Int = 0
    private var allRevenue: Int = 0

    private var startDate: String = ""
    private var endDate: String = ""

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
        getDates(getCurrentMonthStartEndDate()!!)
        showRevenue()

    }

    private fun getDates(monthStartEndDate: Array<String>) {
        startDate = monthStartEndDate[0]
        endDate = monthStartEndDate[1]
    }

    private fun showRevenue() {
        myOrder.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tempList.clear()
                for (data in snapshot.children) {
                    tempList.add(
                        OrderDetailsWithID(
                            data.ref.key.toString(),
                            data.getValue(OrderDetailsModel::class.java)!!
                        )
                    );
                }

                filterOrder(tempList)
            }

            override fun onCancelled(error: DatabaseError) {
                mUtils.mToast(requireContext(), error.message)
            }

        })
    }

    private fun filterOrder(tempList: java.util.ArrayList<OrderDetailsWithID>) {
        orderList.clear()

        //Calculation for all
        for (i in tempList.indices) {
            if (tempList[i].details.seller == mDatabase.uID && tempList[i].details.flag == "Delivered") {
                orderList.add(tempList[i])
            }
        }

        for (i in orderList.indices) {
            allRevenue += orderList[i].details.amount.toInt()
        }

        bind.revenue.text = allRevenue.toString()
        bind.order.text = orderList.size.toString()

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val start: Date = sdf.parse(startDate)
        val end: Date = sdf.parse(endDate)

        //Calculation for month
        for (i in orderList.indices) {
            val orderDate: Date = sdf.parse(formatDate(orderList[i].details.date))
            if (orderDate.after(start) && orderDate.before(end)) {
                monthOrderList.add(orderList[i])

                mLog(formatDate(orderList[i].details.date) + " :: " + startDate + " :: " + endDate)
            }
        }
        for (i in monthOrderList.indices) {
            monthRevenue += monthOrderList[i].details.amount.toInt()
        }

        bind.monthRevenue.text = monthRevenue.toString()
        bind.monthOrder.text = monthOrderList.size.toString()
    }


    private fun initAllComponent() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}