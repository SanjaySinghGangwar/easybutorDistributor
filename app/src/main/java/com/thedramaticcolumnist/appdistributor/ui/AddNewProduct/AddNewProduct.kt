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
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.mProducts
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.Utils.PermissionUtil
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.getTimerStamp
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.hideKeyboard
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.hideLoader
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.isValidText
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mLog
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mToast
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.showLoader
import com.thedramaticcolumnist.appdistributor.databinding.AddNewProductBinding
import com.thedramaticcolumnist.appdistributor.mAdapter.AddNewProductImageAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddNewProduct : Fragment(), View.OnClickListener, AddNewProductImageAdapter.ItemListen,
    CompoundButton.OnCheckedChangeListener {

    private lateinit var addNewProductViewModel: AddNewProductViewModel
    private var _binding: AddNewProductBinding? = null
    var questionTwoAdapter: AddNewProductImageAdapter? = null
    var images: String = ""
    private var splitString: ArrayList<String> = ArrayList()
    val args: AddNewProductArgs by navArgs()

    private lateinit var timestamp: String

    private val bind get() = _binding!!
    val TAG = "ADD PRODUCTS"
    var hashMap: HashMap<String, String> = HashMap<String, String>()
    private var token: String? = null

    companion object {
        fun newInstance() = AddNewProduct()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        addNewProductViewModel =
            ViewModelProvider(this).get(AddNewProductViewModel::class.java)

        _binding = AddNewProductBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        getToken()
        setUpRecycler()

        timestamp = if (args.id.toString().length > 2) {
            fetchProductDetail()
            args.id.toString()
        } else {
            getTimerStamp()
        }
        questionTwoAdapter!!.setItems(splitString)
    }

    private fun fetchProductDetail() {
        mProducts.child(args.id.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild("category")) {
                    val mArray = resources.getStringArray(R.array.categories)
                    bind.category.setSelection(mArray.indexOf(dataSnapshot.child("category").value))
                }
                if (dataSnapshot.hasChild("image")) {
                    images = dataSnapshot.child("image").value.toString()
                    stringToArray(images.substring(1, images.length - 1));
                    questionTwoAdapter!!.setItems(splitString)
                }

                if (dataSnapshot.hasChild("long_description")) {
                    bind.LongDescription.setText(
                        dataSnapshot.child("long_description").value.toString()
                    )
                }
                if (dataSnapshot.hasChild("mrp")) {
                    bind.mrp.setText(
                        dataSnapshot.child("mrp").value.toString()
                    )
                }
                if (dataSnapshot.hasChild("price")) {
                    bind.price.setText(dataSnapshot.child("price").value.toString())
                }
                if (dataSnapshot.hasChild("product_name")) {
                    bind.productName.setText(dataSnapshot.child("product_name").value.toString())


                }
                if (dataSnapshot.hasChild("short_description")) {
                    bind.shortDescription.setText(
                        dataSnapshot.child("short_description").value.toString()
                    )
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

    private fun setUpRecycler() {
        questionTwoAdapter = AddNewProductImageAdapter(requireContext(), this)
        bind.recycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            true
        )
        bind.recycler.adapter = questionTwoAdapter
    }

    private fun initAllComponent() {
        bind.submit.setOnClickListener(this)
        bind.addImages.setOnClickListener(this)
        bind.same.setOnCheckedChangeListener(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addImages -> {
                showLoader(bind.progressBar)
                openGallery(1)
            }
            R.id.submit -> {
                if (isValidText(bind.productName.text.toString().trim(), bind.productName)
                    && isValidText(
                        bind.shortDescription.text.toString().trim(),
                        bind.shortDescription
                    )
                    &&
                    isValidText(bind.LongDescription.text.toString().trim(), bind.LongDescription)
                    && isValidText(bind.mrp.text.toString().trim(), bind.mrp)
                    && isValidText(bind.price.text.toString().trim(), bind.price)
                    && isValidText(
                        bind.quantity.text.toString().trim(), bind.quantity
                    )
                ) {
                    if (!bind.category.selectedItem.equals("Category")) {
                        if (splitString.size > 1) {
                            hashMap["product_name"] = bind.productName.text.toString().trim()
                            hashMap["seller"] =
                                FirebaseAuth.getInstance().currentUser?.uid.toString()
                            hashMap["short_description"] =
                                bind.shortDescription.text.toString().trim()
                            hashMap["long_description"] =
                                bind.LongDescription.text.toString().trim()
                            hashMap["price"] = bind.price.text.toString().trim()
                            hashMap["category"] = bind.category.selectedItem.toString()
                            hashMap["image"] = splitString.toString()
                            hashMap["image_one"] = splitString!![0]
                            hashMap["quantity"] = bind.quantity.text.toString().trim()
                            hashMap["mrp"] = bind.mrp.text.toString().trim()
                            hashMap["sellerToken"] = token.toString()

                            FirebaseDatabase.getInstance().reference.child("Products")
                                .child(timestamp)
                                .setValue(hashMap)
                                .addOnSuccessListener {
                                    val toast = if (args.id.toString().length > 2) {
                                        "Updated"
                                    } else {
                                        "Added"
                                    }
                                    mToast(requireContext(), toast)
                                    hideKeyboard(requireContext())
                                    v.findNavController().navigate(R.id.add_new_product_to_products)
                                    //parentFragmentManager.popBackStack()
                                }.addOnFailureListener {
                                    mToast(requireContext(), it.message.toString())
                                }

                        } else {
                            mToast(requireContext(), "Please upload at least two image")
                        }
                    } else {
                        mToast(requireContext(), "Please select a category")
                    }
                }
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
            if (requestCode == 1) {
                saveProductImage(data!!.data)
            }
        } else {
            hideLoader(bind.progressBar)
        }
    }

    fun saveProductImage(image: Uri?) {

        val random = Random()
        val timestamp =
            SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + random.nextInt(1000000)

        val productStorageRef = FirebaseStorage.getInstance().reference.child("Products/$timestamp")

        productStorageRef.putFile(image!!).addOnCompleteListener { uploadTask ->
            if (uploadTask.isSuccessful) {
                productStorageRef.downloadUrl.addOnSuccessListener {
                    splitString.add(it.toString())
                    questionTwoAdapter!!.setItems(splitString)

                }
            } else {
                mLog(uploadTask.exception?.message.toString())
            }
            hideLoader(bind.progressBar)
        }

    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result
        })
    }

    private fun stringToArray(images: String) {
        splitString = images.split(",") as ArrayList<String>
    }

    override fun delete(index: Int) {
        if (splitString.size > 0) {
            mLog("BEFORE :: $splitString")
            splitString.removeAt(index)
            questionTwoAdapter!!.setItems(splitString)
            mLog("AFTER :: $splitString")
        } else {
            mToast(requireContext(), "Minimum one image is mandatory")
        }

    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.same -> {
                if (isChecked) {
                    bind.price.setText(bind.mrp.text.toString())
                } else {
                    bind.price.setText("")
                }
            }
        }
    }

}