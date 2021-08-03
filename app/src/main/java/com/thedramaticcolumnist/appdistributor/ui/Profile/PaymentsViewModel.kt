package com.thedramaticcolumnist.appdistributor.ui.Profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Your Setting Fragment"
    }
    val text: LiveData<String> = _text
}