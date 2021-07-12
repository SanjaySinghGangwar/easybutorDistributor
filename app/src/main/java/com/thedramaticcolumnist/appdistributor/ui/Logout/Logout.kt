package com.thedramaticcolumnist.appdistributor.ui.Logout

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.lifecycle.Observer
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.databinding.LogoutBinding
import com.thedramaticcolumnist.appdistributor.databinding.MessageBinding
import com.thedramaticcolumnist.appdistributor.ui.Messages.Message
import com.thedramaticcolumnist.appdistributor.ui.Messages.MessageViewModel

class Logout : Fragment() {


    private lateinit var logoutViewModel: LogoutViewModel
    private var _binding: LogoutBinding? = null

    private val bind get() = _binding!!



    companion object {
        fun newInstance() = Logout()
    }

    private lateinit var viewModel: LogoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        logoutViewModel =
            ViewModelProvider(this).get(LogoutViewModel::class.java)

        _binding = LogoutBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textView
        logoutViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAllComponents()
        loadUrl("https://thedramaticcolumnist.com/store-manager/customer-logout/?_wpnonce=2f0c74a12c")
    }

    private fun initAllComponents() {

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