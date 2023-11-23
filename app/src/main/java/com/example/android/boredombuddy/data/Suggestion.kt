package com.example.android.boredombuddy.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Suggestion(
    val id: Long,
    val activity: String,
    val type: String,
    val link: String,
    val imageUrl: String?,
    val notificationTime: Long?
) : Parcelable