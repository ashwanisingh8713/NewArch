package com.ns.shortsnews.user.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ns.shortsnews.user.domain.usecase.user.UserProfileDataUseCase

class UserProfileViewModelFactory: ViewModelProvider.Factory {

    private lateinit var profileDataUseCases: UserProfileDataUseCase

    fun inject(profileDataUseCases: UserProfileDataUseCase){
        this.profileDataUseCases = profileDataUseCases
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserProfileViewModel( profileDataUseCases) as T
    }

}