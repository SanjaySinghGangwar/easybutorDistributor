package com.thedramaticcolumnist.appdistributor.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.smarteist.autoimageslider.SliderView
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mToast
import com.thedramaticcolumnist.appdistributor.databinding.CategoryLayoutBinding
import com.thedramaticcolumnist.appdistributor.databinding.FragmentHomeBinding
import com.thedramaticcolumnist.appdistributor.mAdapter.SliderAdapter
import com.thedramaticcolumnist.appdistributor.mViewHolder.CategoryViewHolder
import com.thedramaticcolumnist.appdistributor.models.ProductModel
import com.thedramaticcolumnist.appdistributor.models.SliderData

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val bind get() = _binding!!
    private lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase

    var url1 =
        "https://media.istockphoto.com/photos/girls-carrying-shopping-bags-picture-id1073935306?k=6&m=1073935306&s=612x612&w=0&h=3vPn17dPt4tAYTfa5izL9BHHE2yV-GPOn3DiN2kTKAo="
    var url2 =
        "https://img.freepik.com/free-photo/female-friends-out-shopping-together_53876-25041.jpg?size=626&ext=jpg"
    var url3 =
        "https://media.istockphoto.com/photos/man-at-the-shopping-picture-id868718238?k=6&m=868718238&s=612x612&w=0&h=ZUPCx8Us3fGhnSOlecWIZ68y3H4rCiTnANtnjHk0bvo="
    var url4 =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAuDC61JrBfNCrPQi_DKAcaaYnO6vWGhPziA&usqp=CAU"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textHome
        homeViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
        setUpSlider()
        setUpCategory()
        setUpOffers()
    }

    private fun setUpOffers() {
        showLoader()
        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(myRef.child("Offers"), ProductModel::class.java)
                .build()
        val recyclerAdapter =
            object : FirebaseRecyclerAdapter<ProductModel, CategoryViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): CategoryViewHolder {
                    val binding: CategoryLayoutBinding =
                        CategoryLayoutBinding.inflate(LayoutInflater.from(parent.context),
                            parent,
                            false)
                    return CategoryViewHolder(requireContext(), binding)
                }

                override fun onBindViewHolder(
                    holder: CategoryViewHolder,
                    position: Int,
                    model: ProductModel,
                ) {
                    hideLoader()
                    holder.bind(model)

                }


            }

        bind.offersRecycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }

    private fun setUpCategory() {
        showLoader()
        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(myRef.child("Categories"), ProductModel::class.java)
                .build()
        val recyclerAdapter =
            object : FirebaseRecyclerAdapter<ProductModel, CategoryViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): CategoryViewHolder {
                    val binding: CategoryLayoutBinding =
                        CategoryLayoutBinding.inflate(LayoutInflater.from(parent.context),
                            parent,
                            false)
                    return CategoryViewHolder(requireContext(), binding)
                }

                override fun onBindViewHolder(
                    holder: CategoryViewHolder,
                    position: Int,
                    model: ProductModel,
                ) {
                    hideLoader()
                    holder.bind(model)

                    holder.card.setOnClickListener {
                        val action =
                            HomeFragmentDirections.homeToViewCategoryProducts(model.name.toString())
                        view?.findNavController()?.navigate(action)
                    }

                }


            }

        bind.categoryRecycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }

    private fun setUpSlider() {
        val sliderDataArrayList: ArrayList<SliderData> = ArrayList()

        val sliderView: SliderView = bind.slider
        sliderDataArrayList.add(SliderData(url1))
        sliderDataArrayList.add(SliderData(url2))
        sliderDataArrayList.add(SliderData(url3))
        sliderDataArrayList.add(SliderData(url4))

        val adapter = SliderAdapter(requireContext(), sliderDataArrayList)

        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderView.setSliderAdapter(adapter)
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        sliderView.startAutoCycle()
    }

    private fun initAllComponents() {
        database = FirebaseDatabase.getInstance()
        myRef = database.reference
        bind.categoryRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        bind.offersRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLoader() {
        if (bind.progressBar.visibility == View.GONE) {
            bind.progressBar.visibility = View.VISIBLE
        }
    }

    fun hideLoader() {
        if (bind.progressBar.visibility == View.VISIBLE) {
            bind.progressBar.visibility = View.GONE
        }
    }
}