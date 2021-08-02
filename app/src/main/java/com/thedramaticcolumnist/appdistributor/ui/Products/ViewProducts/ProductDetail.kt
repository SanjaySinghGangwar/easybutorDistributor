package com.thedramaticcolumnist.appdistributor.ui.Products.ViewProducts

import com.thedramaticcolumnist.appdistributor.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.mID
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mToast
import com.thedramaticcolumnist.appdistributor.databinding.ProductDetailBinding
import com.thedramaticcolumnist.appdistributor.mAdapter.productImagesAdapter
import java.util.*
import kotlin.collections.ArrayList


class ProductDetail : Fragment(), View.OnClickListener {

    private lateinit var splitString: List<String>
    var images: String = ""
    val args: ProductDetailArgs by navArgs()


    lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    private var _binding: ProductDetailBinding? = null
    private val bind get() = _binding!!


    var arrayList = ArrayList<String>()
    var orderDetails: HashMap<String, String> = HashMap<String, String>()
    var productAdapter: productImagesAdapter? = null

    var quantity: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ProductDetailBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        fetchProductDetail()

    }

    private fun initAllComponent() {
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Products")
            .child(args.id)

        bind.edit.setOnClickListener(this)

        bind.recycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false)
        productAdapter = productImagesAdapter(requireContext(), listOf(images))
        bind.recycler.adapter = productAdapter
        productAdapter!!.notifyDataSetChanged()

    }


    private fun fetchProductDetail() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild("category")) {

                }
                if (dataSnapshot.hasChild("image")) {
                    images = dataSnapshot.child("image").value.toString()
                    stringToArray(images.substring(1, images.length - 1));


                    bind.recycler.layoutManager = LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false)
                    productAdapter = productImagesAdapter(requireContext(), splitString)
                    bind.recycler.adapter = productAdapter
                    productAdapter!!.notifyDataSetChanged()

                }

                if (dataSnapshot.hasChild("long_description")) {
                    bind.longDescription.text =
                        dataSnapshot.child("long_description").value.toString()
                }
                if (dataSnapshot.hasChild("price")) {
                    bind.price.text = "₹ " + dataSnapshot.child("price").value.toString()
                }
                if (dataSnapshot.hasChild("product_name")) {
                    bind.name.text = dataSnapshot.child("product_name").value.toString()


                }
                if (dataSnapshot.hasChild("seller")) {
                    if (dataSnapshot.child("seller").value.toString() == mID) {
                        bind.edit.visibility = VISIBLE
                    }

                }
                if (dataSnapshot.hasChild("short_description")) {
                    bind.shortDescription.text =
                        dataSnapshot.child("short_description").value.toString()
                }

                if (dataSnapshot.hasChild("quantity")) {
                    quantity =
                        dataSnapshot.child("quantity").value.toString()
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

    private fun stringToArray(images: String) {
        splitString = images.split(",")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edit -> {
                val action =
                    ProductDetailDirections.productDetailToNavAddNewProduct(args.id)
                view?.findNavController()?.navigate(action)
            }
        }
    }

}