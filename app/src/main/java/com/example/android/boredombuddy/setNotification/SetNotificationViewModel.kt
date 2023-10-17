package com.example.android.boredombuddy.setNotification

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.android.boredombuddy.MainApplication
import com.example.android.boredombuddy.MainViewModel
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.utils.getAlarmManager
import com.example.android.boredombuddy.utils.scheduleNotification
import org.koin.dsl.koinApplication
import java.time.Instant
import java.util.Calendar
import java.util.Date

class SetNotificationViewModel(private val baseViewModel: MainViewModel) : ViewModel() {

    private val _calendar = MutableLiveData(Calendar.getInstance())
    val calendar: LiveData<Calendar>
        get() = _calendar

    private val _launchTimePicker = MutableLiveData(false)

    val launchTimePicker: LiveData<Boolean>
        get() = _launchTimePicker

    val timeInMillis: LiveData<Long> = _calendar.map {
        it.timeInMillis
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
            newCalendar!!.set(Calendar.YEAR, year)
        }

        if (month != null) {
            newCalendar!!.set(Calendar.MONTH, month)
        }


        if (day != null) {
            newCalendar!!.set(Calendar.DAY_OF_MONTH, day)
        }


        if (hour != null) {
            newCalendar!!.set(Calendar.HOUR_OF_DAY, hour)
        }


        if (minute != null) {
            newCalendar!!.set(Calendar.MINUTE, minute)
        }


        _calendar.value = newCalendar

    }

    fun scheduleNotification(context: Context, suggestion: Suggestion) =
        with(context) {
            getAlarmManager().scheduleNotification(this, suggestion, timeInMillis.value!!)
        }

    fun launchTimePicker() {
        _launchTimePicker.value = true
    }

    fun timePickerLaunched() {
        _launchTimePicker.value = false
    }

    fun postToast(message: String) {
        baseViewModel.message.value = message
    }

}