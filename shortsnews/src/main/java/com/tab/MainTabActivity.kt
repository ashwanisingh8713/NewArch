package com.tab

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.ns.shortsnews.data.model.VideoClikedItem
import com.ns.shortsnews.databinding.ActivityMaintabBinding
import com.ns.shortsnews.ui.fragment.LoginFragment
import com.ns.shortsnews.utils.AppPreference
import com.videopager.ui.VideoPagerFragment_2
import com.videopager.utils.CategoryConstants

/**
 * Created by Ashwani Kumar Singh on 19,October,2023.
 */
class MainTabActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMaintabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaintabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val count = 5

        val adapter = TabAdapter(this@MainTabActivity, count)

        for(i in 0..count){
            val videoItems = VideoClikedItem(requiredId = "994", videoFrom = CategoryConstants.DEFAULT_VIDEO_DATA, selectedPosition = 0, loadedVideoData = emptyList())
            val videoPagerFragment = VideoPagerFragment_2()
            val bundle = Bundle()
            bundle.putBoolean("logged_in", AppPreference.isUserLoggedIn)
            bundle.putParcelable("videoItems", videoItems)
            videoPagerFragment.arguments = bundle

            adapter.addFragment(videoPagerFragment, "Category")
//            adapter.addFragment(LoginFragment(), "Category")
        }

//        binding.viewPager.offscreenPageLimit = 0


        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 0
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

    }

}