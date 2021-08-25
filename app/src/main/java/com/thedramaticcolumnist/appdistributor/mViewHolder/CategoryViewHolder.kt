package com.thedramaticcolumnist.appdistributor.mViewHolder

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.databinding.CategoryLayoutBinding
import com.thedramaticcolumnist.appdistributor.models.ProductModel

class CategoryViewHolder(
    private val context: Context,
    private val itemBinding: CategoryLayoutBinding,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var items: ProductModel
    var card: CardView = itemBinding.card


    fun bind(item: ProductModel) {
        this.items = item
        itemBinding.name.text = item.name
        Glide.with(context).load(item.icon).diskCacheStrategy(DiskCacheStrategy.ALL) .error(R.drawable.ic_error).into(itemBinding.image);
    }
}
