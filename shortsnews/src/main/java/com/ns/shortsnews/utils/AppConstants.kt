package com.ns.shortsnews.utils

import android.content.Context
import coil.imageLoader
import com.exo.players.ExoAppPlayerFactory
import com.exo.ui.ExoAppPlayerViewFactory
import com.ns.shortsnews.MainApplication
import com.ns.shortsnews.video.data.VideoDataRepositoryImpl
import com.videopager.ui.VideoPagerFragment
import com.videopager.vm.VideoPagerViewModelFactory

class AppConstants {

    companion object {
        const val PREF_NAME = "NewsDxShorts"

        // Toast constants

        const val  NO_CHANNEL_DATA = "No channel data found"
        const val  FILL_REQUIRED_FIELD = "Please fill all required field"
        const val  FILL_VALID_EMAIL = "Please enter valid Email id"
        const val  FILL_OTP = "Please enter OTP"
        const val  FILL_VALID_OTP = "Please enter valid OTP"
        const val  OTP_VALIDATION_ERROR = "Error validating otp try again"
        const val  SPRINT_TWO = "Coming in sprint 2nd"
        const val  SPRINT_THREE = "Coming in phase 3rd"


        // Toast MSG text
        const val CONNECTIVITY_ERROR_TITLE = "Connectivity Error"
        const val CONNECTIVITY_MSG = "Please ensure you have active internet connection."
        const val API_ERROR_TITLE = "API error"


        fun makeVideoPagerInstance(videoCategory: String, videoFrom: String, context: Context): VideoPagerFragment {
            val appPlayerView =  ExoAppPlayerViewFactory().create(context)
            val vpf =  VideoPagerFragment(
                viewModelFactory = { owner ->
                    VideoPagerViewModelFactory(
                        repository = VideoDataRepositoryImpl(),
                        appPlayerFactory = ExoAppPlayerFactory(
                            context = context, cache = MainApplication.cache
                        ),
                        categoryId = videoCategory,
                        videoFrom = videoFrom
                    ).create(owner)
                },
                appPlayerView = appPlayerView,
                imageLoader = context.imageLoader,
            )
            return vpf
        }



    }
}