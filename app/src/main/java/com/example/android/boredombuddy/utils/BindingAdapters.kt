package com.example.android.boredombuddy.utils

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.android.boredombuddy.R
import com.example.android.boredombuddy.newSuggestion.NewSuggestionViewModel
import com.squareup.picasso.Picasso


@BindingAdapter("showLoading")
fun showLoading(progressBar: ProgressBar, isLoading: Boolean){
    progressBar.isVisible = isLoading
}

@BindingAdapter("setSrc")
fun setImage(view: ImageView, imageUrl: String?){
    Picasso.with(view.context).load(imageUrl)
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.error_image)
        .into(view)
}