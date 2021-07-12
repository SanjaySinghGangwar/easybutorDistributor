package com.thedramaticcolumnist.appdistributor.ui.Dashboard

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
import com.thedramaticcolumnist.appdistributor.databinding.DashboardBinding
import com.thedramaticcolumnist.appdistributor.databinding.MessageBinding
import com.thedramaticcolumnist.appdistributor.ui.Messages.MessageViewModel

class Dashboard : Fragment() {


    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: DashboardBinding? = null

    private val bind get() = _binding!!


    companion object {
        fun newInstance() = Dashboard()
    }

    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = DashboardBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textView
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAllComponents()
        loadUrl("https://thedramaticcolumnist.com/store-manager/")
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