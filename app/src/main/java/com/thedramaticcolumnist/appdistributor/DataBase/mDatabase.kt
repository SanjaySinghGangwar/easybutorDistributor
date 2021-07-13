package com.thedramaticcolumnist.appdistributor.DataBase

import com.google.firebase.storage.FirebaseStorage

class mDatabase {
    val ProductStorageRef = FirebaseStorage.getInstance().reference.child("Products/")
}