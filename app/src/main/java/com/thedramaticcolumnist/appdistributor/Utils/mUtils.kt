package com.thedramaticcolumnist.appdistributor.Utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import java.text.DateFormat
import java.text.DecimalFormat
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

    fun getCurrentMonthStartEndDate(): Array<String>? {
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = 1
        val numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        val startDate: String = getFinalDate(mDay, mMonth, mYear).toString()
        val endDate: String = getFinalDate(numOfDaysInMonth, mMonth, mYear).toString()
        return arrayOf(startDate, endDate)
    }

    fun getFinalDate(year: Int, month: Int, day: Int): String? {
        val mFormat = DecimalFormat("00")
        return year.toString() + "/" + mFormat.format(month + 1) + "/" + mFormat.format(day)
    }

    fun formatDate(input: String): String {
        val inputFormatter: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date: Date = inputFormatter.parse(input)

        val outputFormatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        return outputFormatter.format(date)
    }

}