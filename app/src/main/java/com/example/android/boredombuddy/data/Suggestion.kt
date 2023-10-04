package com.example.android.boredombuddy.data

data class Suggestion(
    val id: Long,
    val activity: String,
    val type: String,
    val link: String,
    val imageUrl: String?
)