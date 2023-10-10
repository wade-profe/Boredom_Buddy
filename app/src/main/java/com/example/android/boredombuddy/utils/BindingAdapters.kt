package com.example.android.boredombuddy.utils

import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.boredombuddy.R
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.favourites.FavouritesListAdapter
import com.squareup.picasso.Picasso


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