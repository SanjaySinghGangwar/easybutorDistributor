package com.thedramaticcolumnist.appdistributor.DataBase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object mDatabase {

    val ProductStorageRef = FirebaseStorage.getInstance().reference.child("Products/")

    var mProducts = FirebaseDatabase.getInstance().reference.child("Products")

    var mID = FirebaseAuth.getInstance().currentUser?.uid

    var myProfile = FirebaseAuth.getInstance().currentUser?.uid?.let {
        FirebaseDatabase.getInstance().reference.child("Easybutor Distributor")
            .child(it)
    }
}