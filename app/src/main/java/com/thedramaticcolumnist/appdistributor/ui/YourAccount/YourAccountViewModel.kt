package com.thedramaticcolumnist.appdistributor.ui.YourAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class YourAccountViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Your Vendor Add Products Fragment"
    }
    val text: LiveData<String> = _text
}