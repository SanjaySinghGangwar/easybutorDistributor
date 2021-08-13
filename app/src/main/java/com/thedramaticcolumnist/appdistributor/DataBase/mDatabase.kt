package com.thedramaticcolumnist.appdistributor.DataBase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

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
    ) {
        mProducts.child(productID).child("quantity").setValue(updatedQuantity)
            .addOnFailureListener { }
            .addOnSuccessListener {
                updateOrderStatus(orderUpdateNode, orderStatus)
            }
    }

    fun updateOrderStatus(orderUpdateNode: String, orderStatus: HashMap<String, String>) {
        myOrder.child(orderUpdateNode).child("Status").setValue(orderStatus)
            .addOnFailureListener { }
            .addOnSuccessListener {
                //sendNotification
            }
    }
}