package com.thedramaticcolumnist.appdistributor.Storage

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

object mFirebaseStorage {


    /*fun saveProductImage(image: Uri?, arrayList: ArrayList<String>): String {
        val TAG = "FIREBASE STORAGE"
        var url = ""
        val random = Random()
        val timestamp =
            SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + random.nextInt(1000000)

        val productStorageRef = FirebaseStorage.getInstance().reference.child("Products/$timestamp")

        productStorageRef.putFile(image!!).addOnCompleteListener { uploadTask ->
            if (uploadTask.isSuccessful) {
                productStorageRef.downloadUrl.addOnSuccessListener {
                    url = it.toString()
                    arrayList.add(url)
                }
            } else {
                Log.i(TAG, "saveImage: ERROR" + uploadTask.exception?.message)
            }
        }
        //Log.i(TAG, "saveProductImage: $url")
        return timestamp
    }*/


}


