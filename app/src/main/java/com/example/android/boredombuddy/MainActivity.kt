package com.example.android.boredombuddy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.android.boredombuddy.favourites.FavouritesFragment
import com.example.android.boredombuddy.newSuggestion.NewSuggestionFragment

const val FRAGMENT_COUNT = 2

class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.view_pager)
        pagerAdapter = PagerAdapter(this)
        viewPager.adapter = pagerAdapter
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Check the current destination fragment to prevent redundant navigation
                val currentDestinationId = navController.currentDestination?.id

                when (position) {
                    0 -> {
                        if (currentDestinationId != R.id.newSuggestionFragment) {
                            navController.navigate(R.id.action_favouritesFragment_to_newSuggestionFragment)
                        }
                    }
                    1 -> {
                        if (currentDestinationId != R.id.favouritesFragment) {
                            navController.navigate(R.id.action_newSuggestionFragment_to_favouritesFragment)
                        }
                    }
                }
            }
        })


    }
}

class PagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> NewSuggestionFragment()
            1 -> FavouritesFragment()
            else -> NewSuggestionFragment()
        }
    }
}