package com.ns.news.presentation.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ns.news.presentation.activity.ui.dashboard.PhotoTabFragment
import com.ns.news.presentation.activity.ui.home.HomeTabFragment
import com.ns.news.presentation.activity.ui.notifications.VideoTabFragment
import com.ns.news.presentation.activity.ui.more.PodcastTabFragment


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class BottomNavPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> HomeTabFragment()
            1-> PhotoTabFragment()
            2-> VideoTabFragment()
            3-> PodcastTabFragment()
            else -> HomeTabFragment()

        }
    }
}