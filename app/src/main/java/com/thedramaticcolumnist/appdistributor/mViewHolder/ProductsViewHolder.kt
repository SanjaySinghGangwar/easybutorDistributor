package com.thedramaticcolumnist.appdistributor.mViewHolder

import android.content.Context
import android.util.Log
import android.view.View.VISIBLE
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.uID
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.databinding.ProductLayoutBinding
import com.thedramaticcolumnist.appdistributor.models.ProductModel

class ProductsViewHolder(
    private val context: Context,
    private val itemBinding: ProductLayoutBinding,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var items: ProductModel
    var card: CardView = itemBinding.card


    fun bind(item: ProductModel) {
        this.items = item
        Log.i("SANJAY ", "bind: $items")

        itemBinding.name.text = item.product_name
        itemBinding.price.text = "₹ " + item.price
        if(item.seller==uID){
            itemBinding.stock.visibility =VISIBLE
            itemBinding.stock.text = "In-Stock : " + item.quantity

        }


        Glide.with(context).load(item.image_one).diskCacheStrategy(DiskCacheStrategy.ALL) .error(R.drawable.ic_error).into(itemBinding.image);
    }
}
