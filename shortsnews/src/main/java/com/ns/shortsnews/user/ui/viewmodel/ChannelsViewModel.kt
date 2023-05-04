package com.ns.shortsnews.user.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ns.shortsnews.user.domain.exception.ApiError
import com.ns.shortsnews.user.domain.models.ChannelsDataResult
import com.ns.shortsnews.user.domain.models.ProfileResult
import com.ns.shortsnews.user.domain.usecase.base.UseCaseResponse
import com.ns.shortsnews.user.domain.usecase.channel.ChannelsDataUseCase
import com.ns.shortsnews.user.domain.usecase.user.UserProfileDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChannelsViewModel(private val channelsDataUseCase: ChannelsDataUseCase): ViewModel() {

    // Profile
    private val _channelsSuccessState = MutableStateFlow<ChannelsDataResult?>(null)
    val ChannelsSuccessState: StateFlow<ChannelsDataResult?> get() = _channelsSuccessState

    private val _errorState = MutableStateFlow<String?>("NA")
    val errorState: StateFlow<String?> get() = _errorState

    private val _loadingState = MutableStateFlow(true)
    val loadingState: MutableStateFlow<Boolean> get() = _loadingState


    fun requestProfileApi() {
        channelsDataUseCase.invoke(viewModelScope, null,
            object : UseCaseResponse<ChannelsDataResult> {
                override fun onSuccess(type: ChannelsDataResult) {
                    _channelsSuccessState.value = type
                    _loadingState.value = false
                }

                override fun onError(apiError: ApiError) {
                    _errorState.value = apiError.getErrorMessage()
                    _loadingState.value = false
                }

                override fun onLoading(isLoading: Boolean) {
                    _loadingState.value = true
                }
            }
        )
    }
}