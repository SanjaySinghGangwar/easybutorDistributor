package com.thedramaticcolumnist.appdistributor.mViewHolder

import android.content.Context
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.databinding.OrderItemLayoutBinding
import com.thedramaticcolumnist.appdistributor.mAdapter.OrderAdapter
import com.thedramaticcolumnist.appdistributor.models.OrderDetailsModel
import com.thedramaticcolumnist.appdistributor.models.OrderDetailsWithID

class OrderViewHolder(
    private val context: Context,
    private val bind: OrderItemLayoutBinding,
    private val listener: OrderAdapter.onClickListner,
) : RecyclerView.ViewHolder(bind.root), View.OnClickListener {


    var card: CardView = bind.card
    private var data: OrderDetailsWithID? = null

    fun bind(item: OrderDetailsWithID) {

        data = item
        card.setOnClickListener(this)
        bind.name.text = item.details.product_name
        bind.price.text = item.details.price
        bind.shortDescription.text = item.details.short_description
        bind.quantity.text = item.details.quantity
        bind.OrderStatus.text = item.details.flag

        Glide.with(context)
            .load(item.details.image_one)
            .placeholder(R.drawable.ic_person)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_error)
            .into(bind.image)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card -> {
                listener.onCLick(data!!.id)
            }
        }
    }


}
