package com.thedramaticcolumnist.appdistributor.ui.Order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Order Fragment"
    }
    val text: LiveData<String> = _text
}