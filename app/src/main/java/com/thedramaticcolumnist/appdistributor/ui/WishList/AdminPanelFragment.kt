package com.thedramaticcolumnist.appdistributor.ui.WishList

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
import com.thedramaticcolumnist.appdistributor.databinding.AdminPanelFragmentBinding

class AdminPanelFragment : Fragment() {

    private lateinit var wishlistViewModel: WishlistViewModel
    private var _binding: AdminPanelFragmentBinding? = null
    private val bind get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        wishlistViewModel =
            ViewModelProvider(this).get(WishlistViewModel::class.java)

        _binding = AdminPanelFragmentBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textSlideshow
        wishlistViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        loadUrl("https://thedramaticcolumnist.com/store-manager/")
    }

    private fun initAllComponent() {

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
}