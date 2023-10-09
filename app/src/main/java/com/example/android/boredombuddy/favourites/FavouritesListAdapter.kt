package com.example.android.boredombuddy.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.boredombuddy.data.Suggestion
import com.example.android.boredombuddy.databinding.SavedSuggestionItemBinding

class FavouritesListAdapter(private val onDeleteClick: (Suggestion) -> Unit,
    private val onNotificationClick: (Suggestion) -> Unit,
    private val onShareClick: (Suggestion) -> Unit):
    ListAdapter<Suggestion, FavouritesListAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback: DiffUtil.ItemCallback<Suggestion>(){
        override fun areItemsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class ViewHolder(var binding: SavedSuggestionItemBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(suggestion: Suggestion){
            binding.suggestion = suggestion
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SavedSuggestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = getItem(position)
        holder.binding.notificationIcon.setOnClickListener {
            onNotificationClick(suggestion)
        }
        holder.binding.deleteIcon.setOnClickListener {
            onDeleteClick(suggestion)
        }
        holder.binding.shareButton.setOnClickListener {
            onShareClick(suggestion)
        }
        holder.bind(suggestion)
    }
}