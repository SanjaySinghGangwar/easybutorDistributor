package com.thedramaticcolumnist.appdistributor.ui.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.ui.BottomSheet.EditProfile.EditProfile
import com.thedramaticcolumnist.app.ui.BottomSheets.LogOut.Logout
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mToast
import com.thedramaticcolumnist.appdistributor.databinding.PaymentsBinding

class MyProfile : Fragment(), View.OnClickListener {


    private var imageUrl: String = ""
    private lateinit var yourAccountViewModel: PaymentsViewModel
    private var _binding: PaymentsBinding? = null

    private val bind get() = _binding!!

    companion object {
        fun newInstance() = MyProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        yourAccountViewModel =
            ViewModelProvider(this).get(PaymentsViewModel::class.java)

        _binding = PaymentsBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textView
        yourAccountViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        fetchProfileData()
        //loadUrl("https://thedramaticcolumnist.com/login/")
    }

    private fun fetchProfileData() {
        FirebaseDatabase.getInstance().reference
            .child(getString(R.string.app_name))
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (bind.progressBar.visibility == View.VISIBLE) {
                        bind.progressBar.visibility = View.GONE
                    }
                    bind.edit.visibility = View.VISIBLE
                    if (snapshot.hasChild("name")) {
                        bind.name.text =
                            snapshot.child("name").value.toString()
                    }
                    if (snapshot.hasChild("phone")) {
                        bind.phone.text =
                            snapshot.child("phone").value.toString()
                    }
                    if (snapshot.hasChild("profile_image")) {
                        imageUrl = snapshot.child("profile_image").value.toString()
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_person)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.ic_error)
                            .into(bind.image);
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (bind.progressBar.visibility == View.VISIBLE) {
                        bind.progressBar.visibility = View.GONE
                    }
                    bind.edit.visibility = View.VISIBLE
                    mToast(requireContext(), error.message)
                }
            })
    }

    private fun initAllComponent() {
        bind.edit.setOnClickListener(this)
        bind.logout.setOnClickListener(this)
        bind.products.setOnClickListener(this)
        bind.orders.setOnClickListener(this)

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
            R.id.edit -> {
                val edit =
                    EditProfile(bind.name.text.toString(),
                        bind.phone.text.toString(),
                        imageUrl)

                edit.show(parentFragmentManager, "")
            }
            R.id.logout -> {
                val logout = Logout()
                logout.show(parentFragmentManager, "")
            }
            R.id.products -> {
                view?.findNavController()?.navigate(R.id.profile_to_products)
            }
            R.id.orders -> {
                view?.findNavController()?.navigate(R.id.profile_to_order)
            }
        }
    }
}

