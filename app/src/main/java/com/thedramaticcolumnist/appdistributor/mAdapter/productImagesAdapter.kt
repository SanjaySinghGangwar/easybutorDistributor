package com.thedramaticcolumnist.appdistributor.mAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.databinding.ProductImageLayoutBinding
import com.thedramaticcolumnist.appdistributor.mViewHolder.ProductsImagesViewHolder

class productImagesAdapter(private val context: Context, private val splitString: List<String>) :
    RecyclerView.Adapter<ProductsImagesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsImagesViewHolder {
        val binding: ProductImageLayoutBinding =
            ProductImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsImagesViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: ProductsImagesViewHolder, position: Int) {
        holder.bind(splitString[position])
        Glide.with(context)
            .load(splitString[position])
            .error(R.drawable.ic_default_product)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView);
    }

    override fun getItemCount(): Int {
        return splitString.size
    }

}