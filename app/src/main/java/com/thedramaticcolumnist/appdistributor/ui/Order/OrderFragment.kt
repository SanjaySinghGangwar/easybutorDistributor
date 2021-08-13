package com.thedramaticcolumnist.appdistributor.ui.Order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.myOrder
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.uID
import com.thedramaticcolumnist.appdistributor.databinding.OrderFragmentBinding
import com.thedramaticcolumnist.appdistributor.databinding.OrderItemLayoutBinding
import com.thedramaticcolumnist.appdistributor.mViewHolder.OrderViewHolder
import com.thedramaticcolumnist.appdistributor.models.ProductModel

class OrderFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private var _binding: OrderFragmentBinding? = null
private val bind get() = _binding!!
    lateinit var recyclerAdapter: FirebaseRecyclerAdapter<ProductModel, OrderViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        orderViewModel =
            ViewModelProvider(this).get(OrderViewModel::class.java)

        _binding = OrderFragmentBinding.inflate(inflater, container, false)
        val root: View = bind.root


        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        showCartData()

    }

    private fun initAllComponent() {

    }

    private fun showCartData() {

        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(myOrder.orderByChild("seller").equalTo(uID), ProductModel::class.java)
                .build()
        recyclerAdapter =
            object : FirebaseRecyclerAdapter<ProductModel, OrderViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): OrderViewHolder {
                    val binding: OrderItemLayoutBinding =
                        OrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context),
                            parent,
                            false)
                    return OrderViewHolder(requireContext(), binding)
                }

                override fun onBindViewHolder(
                    holder: OrderViewHolder,
                    position: Int,
                    model: ProductModel,
                ) {
                    bind.temp.visibility= View.INVISIBLE
                    bind.list.visibility= View.VISIBLE

                    holder.bind(model)
                    holder.card.setOnClickListener{
                        val action = OrderFragmentDirections.orderToOrderDetail(getRef(position).key.toString())
                        view?.findNavController()?.navigate(action)
                    }

                }
            }

        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}