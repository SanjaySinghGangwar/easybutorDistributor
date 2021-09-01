package com.thedramaticcolumnist.appdistributor.ui.OrderDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.mProducts
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.myOrder
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.uID
import com.thedramaticcolumnist.appdistributor.FCM.Notification.sendNotification
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
    private var token: String = ""

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
            myOrder.child(args.id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("flag")) {
                        if (snapshot.hasChild("flag"))
                            bind.status.text = snapshot.child("flag").value.toString()

                        when (snapshot.child("flag").value.toString()) {
                            "Waiting for Shipment detail" -> {
                                bind.trackingLayout.visibility = VISIBLE
                                bind.shippedLayout.visibility = VISIBLE
                                bind.buttonText.text = "UPDATE"
                            }
                            "In-Transit" -> {
                                bind.trackingLayout.visibility = VISIBLE
                                bind.shippedLayout.visibility = VISIBLE
                                bind.buttonText.text = "UPDATE"

                                bind.trackingNumber.setText(snapshot.child("trackingNumber").value.toString())
                                bind.companyName.setText(snapshot.child("companyName").value.toString())
                            }
                            "Waiting for approval" -> {
                                bind.status.text = "Waiting for approval"
                                bind.decline.visibility = VISIBLE
                            }
                            "Cancelled by seller" -> {
                                bind.decline.visibility = GONE
                                bind.approve.visibility = GONE
                            } "Cancelled by buyer" -> {
                                bind.decline.visibility = GONE
                                bind.approve.visibility = GONE
                            }
                        }
                    } else {
                        bind.status.text = "Waiting for approval"
                        bind.decline.visibility = VISIBLE
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
            mLog("product id null")
        }
    }

    private fun showOrderDetail() {
        myOrder.child(args.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("product_id"))
                    product_id = snapshot.child("product_id").value.toString()
                if (snapshot.hasChild("address"))
                    bind.address.text = snapshot.child("address").value.toString()
                if (snapshot.hasChild("price"))
                    bind.price.text = snapshot.child("price").value.toString()
                if (snapshot.hasChild("date"))
                    bind.date.text = snapshot.child("date").value.toString()
                if (snapshot.hasChild("product_name"))
                    bind.name.text = snapshot.child("product_name").value.toString()
                if (snapshot.hasChild("orderId"))
                    bind.orderID.text = snapshot.child("orderId").value.toString()
                if (snapshot.hasChild("quantity"))
                    bind.quantity.text = snapshot.child("quantity").value.toString()
                if (snapshot.hasChild("short_description"))
                    bind.shortDescription.text =
                        snapshot.child("short_description").value.toString()
                if (snapshot.hasChild("buyerToken"))
                    token = snapshot.child("buyerToken").value.toString()
                if (snapshot.hasChild("image_one"))
                    Glide.with(requireContext())
                        .load(snapshot.child("image_one").value.toString())
                        .placeholder(R.drawable.ic_default_product)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_error)
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
        bind.decline.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.approve -> {
                if (seller == uID) {
                    if (bind.status.text.toString().trim() == "Waiting for approval") {
                        if (inStock > bind.quantity.text.toString().trim()) {

                            mProducts.child(product_id).child("quantity")
                                .setValue(inStock.toInt()
                                    .minus(bind.quantity.text.toString().trim().toInt()).toString())

                                .addOnSuccessListener {
                                    myOrder.child(args.id).child("flag")
                                        .setValue("Waiting for Shipment detail")

                                    sendNotification(
                                        "order",
                                        "Status updated for order",
                                        token,
                                        context)
                                }.addOnFailureListener {
                                    mToast(requireContext(), it.message.toString())
                                }

                        }

                    } else if (bind.status.text.toString().trim() == "Waiting for Shipment detail"
                    ) {
                        if (isValidText(bind.companyName.text.toString().trim(), bind.companyName)
                            && isValidText(bind.trackingNumber.text.toString().trim(),
                                bind.trackingNumber)
                        ) {
                            updateOrder()
                        }

                    } else if (bind.status.text.toString().trim() == "In-Transit") {
                        if (isValidText(bind.companyName.text.toString().trim(), bind.companyName)
                            && isValidText(bind.trackingNumber.text.toString().trim(),
                                bind.trackingNumber)
                        ) {
                            updateOrder()
                        }

                    }
                } else {
                    mLog("$seller ::: $inStock ::: $uID")
                    mToast(requireContext(),
                        "There is some issue please contact customer care !!")
                }
            }
            R.id.decline -> {
                myOrder.child(args.id).child("flag")
                    .setValue("Cancelled by seller")
                sendNotification(
                    "order",
                    "Status updated for order",
                    token,
                    context)
            }
        }
    }

    private fun updateOrder() {
        myOrder.child(args.id).child("flag").setValue("In-Transit")
        myOrder.child(args.id).child("companyName")
            .setValue(bind.companyName.text.toString().trim())
        myOrder.child(args.id).child("trackingNumber")
            .setValue(bind.trackingNumber.text.toString().trim())

        sendNotification(
            "order",
            "Status updated for order",
            token,
            context)
    }


}