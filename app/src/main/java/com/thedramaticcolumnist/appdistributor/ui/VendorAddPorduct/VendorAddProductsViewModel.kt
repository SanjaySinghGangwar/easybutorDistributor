package com.thedramaticcolumnist.appdistributor.ui.VendorAddPorduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VendorAddProductsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Your Account Fragment"
    }
    val text: LiveData<String> = _text
}