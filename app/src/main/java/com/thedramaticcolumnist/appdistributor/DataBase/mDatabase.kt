package com.thedramaticcolumnist.appdistributor.DataBase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.thedramaticcolumnist.appdistributor.FCM.Notification.sendNotification

object mDatabase {

    val mDatabase = FirebaseDatabase.getInstance().reference
    val ProductStorageRef = FirebaseStorage.getInstance().reference.child("Products/")

    var mProducts = FirebaseDatabase.getInstance().reference.child("Products")

    var mID = FirebaseAuth.getInstance().currentUser?.uid

    var myProfile = FirebaseAuth.getInstance().currentUser?.uid?.let {
        FirebaseDatabase.getInstance().reference.child("Easybutor Distributor")
            .child(it)
    }

    var myOrder = mDatabase.child("Orders")
    val uID = FirebaseAuth.getInstance().currentUser?.uid


    fun approveOrder(
        productID: String,
        updatedQuantity: String,
        orderUpdateNode: String,
        orderStatus: HashMap<String, String>,
        token: String,
        context: Context,
    ) {
        mProducts.child(productID).child("quantity").setValue(updatedQuantity)
            .addOnFailureListener { }
            .addOnSuccessListener {
                updateOrderStatus(orderUpdateNode, orderStatus,token,context)
            }
    }

    fun updateOrderStatus(
        orderUpdateNode: String,
        orderStatus: HashMap<String, String>,
        token: String,
        context: Context,
    ) {
        myOrder.child(orderUpdateNode).child("Status").setValue(orderStatus)
            .addOnFailureListener { }
            .addOnSuccessListener {
                sendNotification("order", "Status updated for order", token, context)
            }
    }
}