package com.example.android.boredombuddy.data

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Suggestion(
    val id: Long,
    val activity: String,
    val type: String,
    val link: String,
    val imageUrl: String?
) : Parcelable