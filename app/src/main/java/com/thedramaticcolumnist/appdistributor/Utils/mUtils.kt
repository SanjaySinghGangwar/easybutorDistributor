package com.thedramaticcolumnist.appdistributor.Utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

object mUtils {
    fun mToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun mLog(message: String) {
        Log.i("SANJAY", "mLog: $message")
    }

    fun isValidText(text: String?, editText: EditText): Boolean {
        if (TextUtils.isEmpty(text)) {
            editText.requestFocus()
            editText.error = "Mandatory"
            return false
        }
        return true
    }

    fun showLoader(progressBar: ProgressBar) {
        if (progressBar.visibility == GONE) {
            progressBar.visibility = VISIBLE
        }
    }

    fun hideLoader(progressBar: ProgressBar) {
        if (progressBar.visibility == VISIBLE) {
            progressBar.visibility = GONE
        }
    }

    fun getTimerStamp(): String {
        return SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + Random().nextInt(1000000)
    }
}