package com.example.android.boredombuddy.favourites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.boredombuddy.R
import com.example.android.boredombuddy.databinding.FragmentFavouritesBinding
import com.example.android.boredombuddy.newSuggestion.NewSuggestionViewModel
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

        favouritesBinding.favouritesRecyclerView.adapter =
            FavouritesListAdapter(
                {suggestion ->
                    Log.d("WADE", suggestion.activity)
                },
                {suggestion ->
                    Log.d("WADE", suggestion.activity)
                },
                {suggestion ->
                    Log.d("WADE", suggestion.activity)
                }
            )

        viewModel.favouritesList.observe(viewLifecycleOwner){
            Log.i(this::class.java.simpleName, "Observing favourites list")
        }

        return favouritesBinding.root
    }

}