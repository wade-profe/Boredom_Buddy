package com.example.android.boredombuddy.utils

import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter


@BindingAdapter("showLoading")
fun showLoading(progressBar: ProgressBar, isLoading: Boolean){
    progressBar.isVisible = isLoading
}