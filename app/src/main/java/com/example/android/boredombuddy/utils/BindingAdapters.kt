package com.example.android.boredombuddy.utils

import android.opengl.Visibility
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.boredombuddy.R
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.favourites.FavouritesListAdapter
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.SimpleTimeZone


@BindingAdapter("showLoading")
fun showLoading(progressBar: ProgressBar, isLoading: Boolean){
    progressBar.isVisible = isLoading
}

@BindingAdapter("setSrc")
fun setImage(view: ImageView, imageUrl: String?){
        Picasso.with(view.context).load(imageUrl)
            .fit()
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(view)
}

@BindingAdapter("listData")
fun setRecycleViewList(recyclerView: RecyclerView, data: List<Suggestion>?){
    val adapter = recyclerView.adapter as FavouritesListAdapter
    data?.let { adapter.submitList(ArrayList(data)) }
}

@BindingAdapter("setVisibility")
fun setVisibility(view: View, showNoData: Boolean){
    if(showNoData){
        view.visibility = View.VISIBLE
    } else{
        view.visibility = View.INVISIBLE
    }
}

@BindingAdapter("timeText")
fun setTimeText(view: TextView, millis: Long?){
    millis?.let {
        view.text = SimpleDateFormat
            .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
            .format(Date(millis))
    }
}