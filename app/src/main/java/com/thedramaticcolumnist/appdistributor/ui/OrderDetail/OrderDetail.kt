package com.thedramaticcolumnist.appdistributor.ui.OrderDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.approveOrder
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.mProducts
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.myOrder
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.uID
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.updateOrderStatus
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.isValidText
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mLog
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mToast
import com.thedramaticcolumnist.appdistributor.databinding.OrderDetailBinding
import java.util.*


class OrderDetail : Fragment(), View.OnClickListener {

    private var _binding: OrderDetailBinding? = null
    private val bind get() = _binding!!
    val args: OrderDetailArgs by navArgs()

    private var product_id: String = ""
    private var seller: String = ""
    private var inStock: String = ""
    private var orderDetails: HashMap<String, String> = HashMap<String, String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = OrderDetailBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
        showOrderDetail()
        fetchRealTimeStatusOfOrder()

    }

    private fun fetchRealTimeStatusOfOrder() {
        try {
            myOrder.child(args.id).child("Status")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild("flag")) {
                            if (snapshot.hasChild("flag"))
                                bind.status.text = snapshot.child("flag").value.toString()

                            if (snapshot.child("flag").value.toString() == "Waiting for Shipment detail") {
                                bind.trackingLayout.visibility = VISIBLE
                                bind.shippedLayout.visibility = VISIBLE
                                bind.buttonText.text = "UPDATE"
                            } else if (snapshot.child("flag").value.toString() == "In-Transit") {
                                bind.trackingLayout.visibility = VISIBLE
                                bind.shippedLayout.visibility = VISIBLE
                                bind.buttonText.text = "UPDATE"

                                bind.trackingNumber.setText(snapshot.child("trackingNumber").value.toString())
                                bind.companyName.setText(snapshot.child("companyName").value.toString())
                            }

                        } else {
                            bind.status.text = "Waiting for approval"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        mToast(requireContext(), error.message)
                    }

                })
        } catch (e: Exception) {
            mLog("STATUS IS NOT THERE")
        }
    }

    private fun getDetailsOfProductsFromInventory() {
        if (!product_id.isEmpty()) {
            mProducts.child(product_id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    seller = snapshot.child("seller").value.toString()
                    inStock = snapshot.child("quantity").value.toString()

                }

                override fun onCancelled(error: DatabaseError) {
                    mToast(requireContext(), error.message)
                }
            })
        } else {
            //getDetailsOfProductsFromInventory()
            mLog("product id null")
        }
    }

    private fun showOrderDetail() {
        myOrder.child(args.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                product_id = snapshot.child("product_id").value.toString()
                bind.address.text = snapshot.child("address").value.toString()
                bind.price.text = snapshot.child("price").value.toString()
                bind.date.text = snapshot.child("date").value.toString()
                bind.name.text = snapshot.child("product_name").value.toString()
                bind.orderID.text = snapshot.child("orderId").value.toString()
                bind.quantity.text = snapshot.child("quantity").value.toString()
                bind.shortDescription.text = snapshot.child("short_description").value.toString()
                Glide.with(requireContext())
                    .load(snapshot.child("image_one").value.toString())
                    .placeholder(R.drawable.ic_default_product)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(bind.image);
                getDetailsOfProductsFromInventory()
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(requireContext(), error.message)
            }

        })
    }

    private fun initAllComponents() {
        bind.approve.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.approve -> {
                if (bind.status.text.toString().trim() == "Waiting for approval") {
                    orderDetails["flag"] = "Waiting for Shipment detail"
                    if (seller == uID) {
                        if (inStock > bind.quantity.text.toString().trim()) {
                            approveOrder(product_id,
                                inStock.toInt().minus(bind.quantity.text.toString().trim().toInt())
                                    .toString(),
                                args.id,
                                orderDetails
                            )
                        }
                    } else {
                        mLog("$seller ::: $inStock ::: $uID")
                        mToast(requireContext(),
                            "There is some issue please contact customer care !!")
                    }
                } else if (bind.status.text.toString().trim() == "Waiting for Shipment detail") {
                    if (isValidText(bind.companyName.text.toString().trim(), bind.companyName)
                        && isValidText(bind.trackingNumber.text.toString().trim(),
                            bind.trackingNumber)
                    ) {
                        orderDetails["flag"] = "In-Transit"
                        orderDetails["companyName"] = bind.companyName.text.toString().trim()
                        orderDetails["trackingNumber"] = bind.trackingNumber.text.toString().trim()
                        updateOrderStatus(args.id, orderDetails)
                    }

                } else if (bind.status.text.toString().trim() == "In-Transit") {
                    if (isValidText(bind.companyName.text.toString().trim(), bind.companyName)
                        && isValidText(bind.trackingNumber.text.toString().trim(),
                            bind.trackingNumber)
                    ) {
                        orderDetails["flag"] = "In-Transit"
                        orderDetails["companyName"] = bind.companyName.text.toString().trim()
                        orderDetails["trackingNumber"] = bind.trackingNumber.text.toString().trim()
                        updateOrderStatus(args.id, orderDetails)
                    }

                }
            }
        }
    }


}