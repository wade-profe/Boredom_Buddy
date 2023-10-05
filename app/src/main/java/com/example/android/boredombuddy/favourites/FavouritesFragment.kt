package com.example.android.boredombuddy.favourites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.boredombuddy.R
import com.example.android.boredombuddy.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment() {

    private lateinit var favouritesBinding: FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favouritesBinding = FragmentFavouritesBinding.inflate(inflater, container, false)

        return favouritesBinding.root
    }

}