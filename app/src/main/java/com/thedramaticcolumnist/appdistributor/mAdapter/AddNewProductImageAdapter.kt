package com.thedramaticcolumnist.appdistributor.mAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.appdistributor.databinding.AddPrductImageLayoutBinding
import com.thedramaticcolumnist.appdistributor.mViewHolder.AddNewProductImageViewHolder

class AddNewProductImageAdapter(private val context: Context, private val listener: ItemListen) :
    RecyclerView.Adapter<AddNewProductImageViewHolder>() {

    interface ItemListen {
        fun delete(index: Int)
    }


    private val items = ArrayList<String>()

    fun setItems(items: ArrayList<String>) {
        if (items.size == 0) {
            this.items.clear()
            this.items.add("https://firebasestorage.googleapis.com/v0/b/easybutor.appspot.com/o/abc.jpg?alt=media&token=354b86d5-4674-4f2b-b5a3-b0e25d2121cf")
            notifyDataSetChanged()
        } else {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AddNewProductImageViewHolder {
        val binding: AddPrductImageLayoutBinding =
            AddPrductImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddNewProductImageViewHolder(binding, context, listener)
    }

    override fun onBindViewHolder(holder: AddNewProductImageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


}
