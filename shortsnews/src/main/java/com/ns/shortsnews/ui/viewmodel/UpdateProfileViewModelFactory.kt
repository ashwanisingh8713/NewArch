package com.ns.shortsnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ns.shortsnews.domain.usecase.updateuser.UpdateUserUseCase
import com.ns.shortsnews.domain.usecase.user.UserProfileDataUseCase

class UpdateProfileViewModelFactory: ViewModelProvider.Factory {

    private lateinit var updateProfileDataUseCases: UpdateUserUseCase

    fun inject(updateProfileDataUseCase: UpdateUserUseCase){
        this.updateProfileDataUseCases = updateProfileDataUseCase
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpdateProfileViewModel( updateProfileDataUseCases) as T
    }

}