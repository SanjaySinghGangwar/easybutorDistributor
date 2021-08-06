package com.thedramaticcolumnist.appdistributor.ui.AddNewProduct

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.mProducts
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.Utils.PermissionUtil
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.isValidText
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mToast
import com.thedramaticcolumnist.appdistributor.databinding.AddNewProductBinding
import java.text.SimpleDateFormat
import java.util.*


class AddNewProduct : Fragment(), View.OnClickListener {

    private lateinit var addNewProductViewModel: AddNewProductViewModel
    private var _binding: AddNewProductBinding? = null


    val args: AddNewProductArgs by navArgs()

    private val bind get() = _binding!!
    val TAG = "ADD PRODUCTS"
    val arrayList = ArrayList<String>()
    var hashMap: HashMap<String, String> = HashMap<String, String>()

    companion object {
        fun newInstance() = AddNewProduct()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        addNewProductViewModel =
            ViewModelProvider(this).get(AddNewProductViewModel::class.java)

        _binding = AddNewProductBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        if (args.id != null) {

            fetchProductDetail()
        }


    }

    private fun fetchProductDetail() {
        mProducts.child(args.id.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild("category")) {
                    val mArray = resources.getStringArray(R.array.categories)
                    bind.category.setSelection(listOf(mArray)
                        .indexOf(dataSnapshot.child("category").value))
                }
                if (dataSnapshot.hasChild("image")) {

                }

                if (dataSnapshot.hasChild("long_description")) {
                    bind.LongDescription.setText(
                        dataSnapshot.child("long_description").value.toString())
                }
                if (dataSnapshot.hasChild("mrp")) {
                    bind.mrp.setText(
                        dataSnapshot.child("mrp").value.toString())
                }
                if (dataSnapshot.hasChild("price")) {
                    bind.price.setText(dataSnapshot.child("price").value.toString())
                }
                if (dataSnapshot.hasChild("product_name")) {
                    bind.productName.setText(dataSnapshot.child("product_name").value.toString())


                }
                if (dataSnapshot.hasChild("short_description")) {
                    bind.shortDescription.setText(
                        dataSnapshot.child("short_description").value.toString())
                }

                if (dataSnapshot.hasChild("quantity")) {
                    bind.quantity.setText(dataSnapshot.child("quantity").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(
                    context?.applicationContext!!,
                    error.toException().message.toString()
                )
            }
        })
    }

    private fun initAllComponent() {
        bind.imageOne.setOnClickListener(this)
        bind.imageTwo.setOnClickListener(this)
        bind.imageThree.setOnClickListener(this)
        bind.imageFour.setOnClickListener(this)
        bind.imageFive.setOnClickListener(this)
        bind.imageSix.setOnClickListener(this)
        bind.imageSeven.setOnClickListener(this)
        bind.imageEight.setOnClickListener(this)
        bind.submit.setOnClickListener(this)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageOne -> {
                openGallery(1)
            }
            R.id.imageTwo -> {
                openGallery(2)
            }
            R.id.imageThree -> {
                openGallery(3)
            }
            R.id.imageFour -> {
                openGallery(4)
            }
            R.id.imageFive -> {
                openGallery(5)
            }
            R.id.imageSix -> {
                openGallery(6)
            }
            R.id.imageSeven -> {
                openGallery(7)
            }
            R.id.imageEight -> {
                openGallery(8)
            }
            R.id.submit -> {
                if (isValidText(bind.productName.text.toString().trim(), bind.productName)
                    && isValidText(bind.shortDescription.text.toString().trim(),
                        bind.shortDescription)
                    &&
                    isValidText(bind.LongDescription.text.toString().trim(), bind.LongDescription)
                    && isValidText(bind.mrp.text.toString().trim(), bind.mrp)
                    && isValidText(bind.price.text.toString().trim(), bind.price)
                    && isValidText(bind.quantity.text.toString().trim(), bind.quantity
                    )
                ) {
                    if (!bind.category.selectedItem.equals("Category")) {
                        if (!arrayList.isNullOrEmpty()) {

                            var timestamp = if (args.id != null&& args.id.toString().isNotEmpty()) {
                                args.id.toString()
                            } else {
                                SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + Random().nextInt(
                                    1000000)
                            }


                            hashMap["product_name"] = bind.productName.text.toString().trim()
                            hashMap["seller"] =
                                FirebaseAuth.getInstance().currentUser?.uid.toString()
                            hashMap["short_description"] =
                                bind.shortDescription.text.toString().trim()
                            hashMap["long_description"] =
                                bind.LongDescription.text.toString().trim()
                            hashMap["price"] = bind.price.text.toString().trim()
                            hashMap["category"] = bind.category.selectedItem.toString()
                            hashMap["subCategory"] = bind.category.selectedItem.toString()
                            hashMap["image"] = arrayList.toString()
                            hashMap["image_one"] = arrayList[0]
                            hashMap["quantity"] = bind.quantity.text.toString().trim()
                            hashMap["mrp"] = bind.mrp.text.toString().trim()

                            FirebaseDatabase.getInstance().reference.child("Products")
                                .child(timestamp)
                                .setValue(hashMap)
                                .addOnSuccessListener {

                                    var toast = if (args.id != null) {
                                        "Updated"
                                    } else {
                                        "Added"
                                    }
                                    mToast(requireContext(), toast)
                                    parentFragmentManager.popBackStack()
                                }.addOnFailureListener {
                                    mToast(requireContext(), it.message.toString())
                                }

                        } else {
                            mToast(requireContext(), "Please upload a image")
                        }

                    } else {
                        mToast(requireContext(), "Please select a category")
                    }
                }
                Log.i(TAG, "onClick: " + arrayList + " :: " + arrayList.size)
            }
        }
    }

    private fun openGallery(i: Int) {
        if (PermissionUtil.verifyPermissions(
                requireContext(),
                PermissionUtil.getGalleryPermissions()
            )
        ) {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, i)

        } else PermissionUtil.requestPermission(
            PermissionUtil.getGalleryPermissions(),
            requireActivity()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    setImage(data!!.data, bind.imageOne)
                    saveProductImage(data.data)
                }
                2 -> {
                    setImage(data!!.data, bind.imageTwo)
                    saveProductImage(data.data)
                }
                3 -> {
                    setImage(data!!.data, bind.imageThree)
                    saveProductImage(data.data)
                }
                4 -> {
                    setImage(data!!.data, bind.imageFour)
                    saveProductImage(data.data)
                }
                5 -> {
                    setImage(data!!.data, bind.imageFive)
                    saveProductImage(data.data)
                }
                6 -> {
                    setImage(data!!.data, bind.imageSix)
                    saveProductImage(data.data)
                }
                7 -> {
                    setImage(data!!.data, bind.imageSeven)
                    saveProductImage(data.data)
                }
                8 -> {
                    setImage(data!!.data, bind.imageEight)
                    saveProductImage(data.data)
                }
            }
        }
    }

    private fun setImage(image: Uri?, imageView: ImageView) {
        Glide
            .with(requireContext())
            .load(image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.drawable.ic_default_product)
            .into(imageView);
    }

    fun saveProductImage(image: Uri?) {
        val TAG = "FIREBASE STORAGE"
        var url = ""
        val random = Random()
        val timestamp =
            SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + random.nextInt(1000000)

        val productStorageRef = FirebaseStorage.getInstance().reference.child("Products/$timestamp")

        productStorageRef.putFile(image!!).addOnCompleteListener { uploadTask ->
            if (uploadTask.isSuccessful) {
                productStorageRef.downloadUrl.addOnSuccessListener {
                    arrayList.add(it.toString())
                }
            } else {
                Log.i(TAG, "saveImage: ERROR" + uploadTask.exception?.message)
            }
        }

    }

}