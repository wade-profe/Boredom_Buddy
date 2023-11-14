package com.example.android.boredombuddy.setNotification

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.data.SuggestionRepository
import com.example.android.boredombuddy.utils.getAlarmManager
import com.example.android.boredombuddy.utils.scheduleNotification
import kotlinx.coroutines.launch
import java.util.Calendar

enum class ResultMessage {
    DEFAULT,
    INVALID_TIME,
    SUCCESS
}

class SetNotificationViewModel(private val repository: SuggestionRepository) : ViewModel() {

    private val _calendar = MutableLiveData(Calendar.getInstance())
    val calendar: LiveData<Calendar>
        get() = _calendar

    private val _launchTimePicker = MutableLiveData(false)

    val launchTimePicker: LiveData<Boolean>
        get() = _launchTimePicker

    val timeInMillis: LiveData<Long> = _calendar.map {
        it.timeInMillis
    }

    private var _resultMessage = MutableLiveData(ResultMessage.DEFAULT)
    val resultMessage: LiveData<ResultMessage>
        get() = _resultMessage

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

    fun scheduleNotification(context: Context, suggestion: Suggestion) {
        viewModelScope.launch {
            if (Calendar.getInstance().timeInMillis >= timeInMillis.value!!) {
                _resultMessage.value = ResultMessage.INVALID_TIME
            } else {
                repository.storeAlarmTime(suggestion.id, timeInMillis.value!!)
                context.getAlarmManager()
                    .scheduleNotification(context, suggestion, timeInMillis.value!!)
                _resultMessage.value = ResultMessage.SUCCESS
            }
        }
    }

    fun launchTimePicker() {
        _launchTimePicker.value = true
    }

    fun timePickerLaunched() {
        _launchTimePicker.value = false
    }

}