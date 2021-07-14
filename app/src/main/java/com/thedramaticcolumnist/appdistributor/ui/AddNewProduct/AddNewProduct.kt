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
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
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


    private val bind get() = _binding!!
    val TAG = "ADD PRODUCTS"
    val arrayList = ArrayList<String>()
    var hashMap: HashMap<String, String> = HashMap<String, String>()

    companion object {
        fun newInstance() = AddNewProduct()
    }

    private lateinit var viewModel: AddNewProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        addNewProductViewModel =
            ViewModelProvider(this).get(AddNewProductViewModel::class.java)

        _binding = AddNewProductBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textView
        addNewProductViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        //loadUrl("https://thedramaticcolumnist.com/store-manager/products-manage/")
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

    fun loadUrl(url: String) {
        val settings: WebSettings = bind.webView.getSettings()
        settings.domStorageEnabled = true

        bind.webView.requestFocus();
        bind.webView.settings.lightTouchEnabled = true;
        bind.webView.settings.javaScriptEnabled = true;
        bind.webView.settings.setGeolocationEnabled(true);
        bind.webView.isSoundEffectsEnabled = true;
        bind.webView.settings.setAppCacheEnabled(true);
        bind.webView.loadUrl(url);
        bind.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                bind.progressBar.visibility = View.VISIBLE
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                bind.progressBar.visibility = View.GONE
            }
        }
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
                    && isValidText(bind.price.text.toString().trim(), bind.price)
                    && isValidText(bind.quantity.text.toString().trim(), bind.quantity
                    )
                ) {
                    if (!bind.category.selectedItem.equals("Category")) {
                        if (!bind.subCategory.selectedItem.equals("Sub-Category")) {
                            if (!arrayList[0].isNullOrBlank()) {
                                val timestamp =
                                    SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + Random().nextInt(
                                        1000000)

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

                                FirebaseDatabase.getInstance().reference.child("Products")
                                    .child(timestamp)
                                    .setValue(hashMap)
                                    .addOnSuccessListener {
                                        mToast(requireContext(), "Added")
                                        parentFragmentManager.popBackStack()
                                    }

                            } else {
                                mToast(requireContext(), "Please upload a image")
                            }
                        } else {
                            mToast(requireContext(), "Please select a sub category")
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