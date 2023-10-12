package com.example.android.boredombuddy.setNotification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import java.util.Calendar

class SetNotificationViewModel : ViewModel() {

    private val _calendar = MutableLiveData(Calendar.getInstance())
    val calendar: LiveData<Calendar>
        get() = _calendar

    private val _launchTimePicker = MutableLiveData(false)

    val launchTimePicker: LiveData<Boolean>
        get() = _launchTimePicker

    val timeInMillis: LiveData<Long> = _calendar.map {
        it.timeInMillis
    }

    init {
        Log.d("WADE", this.toString())
//        calendar.value = Calendar.getInstance()
    }

    fun updateCalendar(
        year: Int? = null,
        month: Int? = null,
        day: Int? = null,
        hour: Int? = null,
        minute: Int? = null
    ) {

        val newCalendar = _calendar.value

        if (year != null) {
            Log.d("WADE", "Setting year")
            newCalendar!!.set(Calendar.YEAR, year)
        }

        if (month != null) {
            Log.d("WADE", "Setting month")
            newCalendar!!.set(Calendar.MONTH, month)
        }


        if (day != null) {
            Log.d("WADE", "Setting day")
            newCalendar!!.set(Calendar.DAY_OF_MONTH, day)
        }


        if (hour != null) {
            Log.d("WADE", "Setting hour")
            newCalendar!!.set(Calendar.HOUR_OF_DAY, hour)
        }


        if (minute != null) {
            Log.d("WADE", "Setting minute")
            newCalendar!!.set(Calendar.MINUTE, minute)
        }


        _calendar.value = newCalendar

    }

    fun launchTimePicker() {
        _launchTimePicker.value = true
    }

    fun timePickerLaunched() {
        _launchTimePicker.value = false
    }


}