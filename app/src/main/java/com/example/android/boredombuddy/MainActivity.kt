package com.example.android.boredombuddy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.android.boredombuddy.favourites.FavouritesFragment
import com.example.android.boredombuddy.newSuggestion.NewSuggestionFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject

const val FRAGMENT_COUNT = 2

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var pagerAdapter: PagerAdapter
    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        pagerAdapter = PagerAdapter(this)
        viewPager.adapter = pagerAdapter
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

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

        viewModel.message.observe(this) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
        }
    }

    // TODO implement suggestion save
    // TODO implement favourites
}

class PagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return FRAGMENT_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> NewSuggestionFragment()
            1 -> FavouritesFragment()
            else -> NewSuggestionFragment()
        }
    }
}