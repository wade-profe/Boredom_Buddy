package com.example.android.boredombuddy.utils

import java.text.SimpleDateFormat
import java.util.Date

fun Long?.getDateTime(): String? {
    this?.let {
        return SimpleDateFormat
            .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
            .format(Date(this))
    } ?: return null
}