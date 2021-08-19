package com.thedramaticcolumnist.appdistributor.mViewHolder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mLog
import com.thedramaticcolumnist.appdistributor.databinding.AddPrductImageLayoutBinding
import com.thedramaticcolumnist.appdistributor.mAdapter.AddNewProductImageAdapter

class AddNewProductImageViewHolder(
    private val bind: AddPrductImageLayoutBinding,
    private val context: Context,
    private val listener: AddNewProductImageAdapter.ItemListen,

    ) :
    RecyclerView.ViewHolder(bind.root), View.OnClickListener {


    fun bind(item: String) {
        mLog(item)
        bind.delete.setOnClickListener(this)
        Glide
            .with(context)
            .load(item)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.drawable.ic_default_product)
            .into(bind.image);
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.delete -> {
                listener.delete(adapterPosition)
            }
        }
    }

}
