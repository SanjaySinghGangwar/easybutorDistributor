package com.thedramaticcolumnist.appdistributor.ui.Order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.myOrder
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.uID
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mLog
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mToast
import com.thedramaticcolumnist.appdistributor.databinding.OrderFragmentBinding
import com.thedramaticcolumnist.appdistributor.mAdapter.OrderAdapter
import com.thedramaticcolumnist.appdistributor.mViewHolder.OrderViewHolder
import com.thedramaticcolumnist.appdistributor.models.OrderDetailsModel
import com.thedramaticcolumnist.appdistributor.models.OrderDetailsWithID

class OrderFragment : Fragment(), View.OnClickListener, OrderAdapter.onClickListner {

    private lateinit var orderViewModel: OrderViewModel
    private var _binding: OrderFragmentBinding? = null
    private val bind get() = _binding!!
    lateinit var recyclerAdapter: FirebaseRecyclerAdapter<OrderDetailsModel, OrderViewHolder>


    private var orderAdapter: OrderAdapter? = null


    private var tempList: ArrayList<OrderDetailsWithID> = ArrayList()
    private var orderList: ArrayList<OrderDetailsWithID> = ArrayList()
    private var waitingList: ArrayList<OrderDetailsWithID> = ArrayList()
    private var inTransitList: ArrayList<OrderDetailsWithID> = ArrayList()
    private var deliveredList: ArrayList<OrderDetailsWithID> = ArrayList()
    private var cancelledList: ArrayList<OrderDetailsWithID> = ArrayList()


    private var ALlList: ArrayList<OrderDetailsWithID> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        orderViewModel =
            ViewModelProvider(this).get(OrderViewModel::class.java)

        _binding = OrderFragmentBinding.inflate(inflater, container, false)
        val root: View = bind.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        setUpRecyclerView()
        getListFromFirebase()
    }

    private fun getListFromFirebase() {
        myOrder.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tempList.clear()
                for (data in snapshot.children) {
                    tempList.add(
                        OrderDetailsWithID(data.ref.key.toString(),
                            data.getValue(OrderDetailsModel::class.java)!!)
                    );
                }
                bind.temp.visibility = View.INVISIBLE
                bind.list.visibility = View.VISIBLE
                filterUID(tempList)
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(requireContext(), error.message)
            }

        })
    }

    private fun filterUID(tempList: java.util.ArrayList<OrderDetailsWithID>) {
        orderList.clear()
        for (i in tempList.indices) {
            if (tempList[i].details.seller == uID) {

                orderList.add(tempList[i])
            }
        }
        orderAdapter!!.setItems(orderList)
    }

    private fun setUpRecyclerView() {
        bind.recycler.layoutManager = LinearLayoutManager(requireContext())
        bind.recycler.hasFixedSize()
        orderAdapter = OrderAdapter(requireContext(), this)
        bind.recycler.adapter = orderAdapter
    }

    private fun initAllComponent() {
        bind.inTransit.setOnClickListener(this)
        bind.waiting.setOnClickListener(this)
        bind.delivered.setOnClickListener(this)
        bind.cancelled.setOnClickListener(this)
        bind.all.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.waiting -> {
                waitingList.clear()
                for (i in orderList.indices) {
                    if (orderList[i].details.flag.toString() == "Waiting for approval") {
                        waitingList.add(orderList[i])
                    }
                }
                orderAdapter!!.setItems(waitingList)

            }
            R.id.inTransit -> {
                inTransitList.clear()
                for (i in orderList.indices) {
                    if (orderList[i].details.flag.toString() == "In-Transit") {
                        inTransitList.add(orderList[i])

                    }
                }
                orderAdapter!!.setItems(inTransitList)

            }
            R.id.cancelled -> {
                cancelledList.clear()
                for (i in orderList.indices) {
                    if (orderList[i].details.flag.toString() == "Cancelled by seller") {
                        cancelledList.add(orderList[i])
                    }
                }
                orderAdapter!!.setItems(cancelledList)
            }
            R.id.delivered -> {
                deliveredList.clear()
                for (i in orderList.indices) {
                    if (orderList[i].details.flag.toString() == "Delivered") {
                        deliveredList.add(orderList[i])
                    }
                }
                orderAdapter!!.setItems(deliveredList)

            }
            R.id.all -> {
                orderAdapter!!.setItems(orderList)

            }
        }
    }


    override fun onCLick(id: String) {
        val action = OrderFragmentDirections.orderToOrderDetail(id)
        view?.findNavController()?.navigate(action)
    }


}