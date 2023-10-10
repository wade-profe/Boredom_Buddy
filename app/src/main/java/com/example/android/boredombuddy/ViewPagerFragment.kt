package com.example.android.boredombuddy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.android.boredombuddy.databinding.FragmentViewPagerBinding
import com.example.android.boredombuddy.favourites.FavouritesFragment
import com.example.android.boredombuddy.newSuggestion.NewSuggestionFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

const val FRAGMENT_COUNT = 2
class ViewPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var binding: FragmentViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        pagerAdapter = PagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        // Inflate the layout for this fragment
        return binding.root
    }
}


class PagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return FRAGMENT_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewSuggestionFragment()
            1 -> FavouritesFragment()
            else -> NewSuggestionFragment()
        }
    }
}