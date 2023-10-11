package com.example.android.boredombuddy.favourites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.boredombuddy.databinding.FragmentFavouritesBinding
import org.koin.android.ext.android.inject

class FavouritesFragment : Fragment() {

    private lateinit var favouritesBinding: FragmentFavouritesBinding
    private val viewModel: FavouritesViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favouritesBinding = FragmentFavouritesBinding.inflate(inflater, container, false)
        favouritesBinding.lifecycleOwner =this
        favouritesBinding.viewModel = viewModel
        val adapter = FavouritesListAdapter(
            {suggestion ->
                viewModel.deleteSuggestion(suggestion.id)
            },
            {suggestion ->
                Log.d("WADE", suggestion.activity)
            },
            {suggestion ->
                Log.d("WADE", suggestion.activity)
            }
        )

        favouritesBinding.favouritesRecyclerView.adapter = adapter

        return favouritesBinding.root
    }

}