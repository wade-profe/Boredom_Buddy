package com.example.android.boredombuddy.setNotification

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import java.util.Calendar

class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object{
        val TAG = "date_picker_fragment"
    }

    val viewModel: SetNotificationViewModel by viewModels({ requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = viewModel.calendar.value?.get(Calendar.YEAR)
        val month = viewModel.calendar.value?.get(Calendar.MONTH)
        val day = viewModel.calendar.value?.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year!!, month!!, day!!)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.updateCalendar(
            year = year,
            month = month,
            day = dayOfMonth
        )
        viewModel.launchTimePicker()


        // TODO create field in SetNotification fragment that displays the current/selected date and time by observing viewmodel
    }


}