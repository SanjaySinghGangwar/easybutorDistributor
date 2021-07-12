package com.thedramaticcolumnist.appdistributor.ui.Messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Your Products Fragment"
    }
    val text: LiveData<String> = _text
}