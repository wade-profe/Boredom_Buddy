package com.example.android.boredombuddy.setNotification

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels

import java.util.Calendar

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    companion object{
        val TAG = "time_picker_fragment"
    }

    val viewModel: SetNotificationViewModel by viewModels({ requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = viewModel.calendar.value!!.get(Calendar.HOUR_OF_DAY)
        val minute = viewModel.calendar.value!!.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.updateCalendar(
            hour = hourOfDay,
            minute = minute
        )
    }
}