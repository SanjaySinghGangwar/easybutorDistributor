package com.thedramaticcolumnist.appdistributor.mAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.appdistributor.databinding.OrderItemLayoutBinding
import com.thedramaticcolumnist.appdistributor.mViewHolder.OrderViewHolder
import com.thedramaticcolumnist.appdistributor.models.OrderDetailsModel
import com.thedramaticcolumnist.appdistributor.models.OrderDetailsWithID

class OrderAdapter(
    private val context: Context,
    private val listener: onClickListner,
) : RecyclerView.Adapter<OrderViewHolder>() {

    interface onClickListner {
        fun onCLick(id: String)
    }

    private val items = ArrayList<OrderDetailsWithID>()

    fun setItems(items: ArrayList<OrderDetailsWithID>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OrderViewHolder {
        val binding: OrderItemLayoutBinding =
            OrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(context, binding, listener)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}