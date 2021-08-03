package com.thedramaticcolumnist.appdistributor.ui.Order

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
import com.thedramaticcolumnist.appdistributor.databinding.OrderFragmentBinding

class OrderFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private var _binding: OrderFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val bind get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        orderViewModel =
            ViewModelProvider(this).get(OrderViewModel::class.java)

        _binding = OrderFragmentBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textView
        orderViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()

    }

    private fun initAllComponent() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}