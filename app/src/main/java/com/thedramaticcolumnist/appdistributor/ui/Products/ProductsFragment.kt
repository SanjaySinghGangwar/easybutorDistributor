package com.thedramaticcolumnist.appdistributor.ui.Products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.mID
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.mProducts
import com.thedramaticcolumnist.appdistributor.databinding.ProductLayoutBinding
import com.thedramaticcolumnist.appdistributor.databinding.ProductsFragmentBinding
import com.thedramaticcolumnist.appdistributor.mViewHolder.ProductsViewHolder
import com.thedramaticcolumnist.appdistributor.models.ProductModel
import com.thedramaticcolumnist.appdistributor.ui.BottomSheet.confirmation.DeleteConfirmation

class ProductsFragment : Fragment() {

    private lateinit var productsAccountViewModel: ProductsViewModel
    private var _binding: ProductsFragmentBinding? = null

    private val bind get() = _binding!!

    companion object {
        fun newInstance() = ProductsFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        productsAccountViewModel =
            ViewModelProvider(this).get(ProductsViewModel::class.java)

        _binding = ProductsFragmentBinding.inflate(inflater, container, false)
        val root: View = bind.root

        /* val textView: TextView = bind.textView
         productsAccountViewModel.text.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })*/
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAllComponents()
    }

    private fun initAllComponents() {
        bind.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        initRecycler()

    }

    private fun initRecycler() {
        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(
                    mProducts.orderByChild("seller").equalTo(mID),
                    ProductModel::class.java
                )
                .build()
        val recyclerAdapter =
            object : FirebaseRecyclerAdapter<ProductModel, ProductsViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): ProductsViewHolder {
                    val binding: ProductLayoutBinding =
                        ProductLayoutBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    return ProductsViewHolder(requireContext(), binding)
                }

                override fun onBindViewHolder(
                    holder: ProductsViewHolder,
                    position: Int,
                    model: ProductModel,
                ) {
                    bind.progressBar.visibility = View.GONE
                    holder.bind(model)
                    holder.card.setOnClickListener {
                        val action =
                            ProductsFragmentDirections.productsToProductDetail(getRef(position).key.toString())
                        view?.findNavController()?.navigate(action)
                    }
                    holder.delete.visibility = VISIBLE
                    holder.delete.setOnClickListener {
                        val delete = DeleteConfirmation(getRef(position).key.toString())
                        delete.showNow(parentFragmentManager, "show")
                    }
                }
            }

        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }
}