package com.ns.shortsnews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.ns.shortsnews.data.mapper.UserVideoCategory
import com.ns.shortsnews.domain.exception.ApiError
import com.ns.shortsnews.domain.models.UpdateCategories
import com.ns.shortsnews.domain.models.VideoCategoryResult
import com.ns.shortsnews.domain.usecase.base.UseCaseResponse
import com.ns.shortsnews.domain.usecase.video_category.UpdateVideoCategoriesUseCase
import com.ns.shortsnews.domain.usecase.video_category.VideoCategoryUseCase
import com.ns.shortsnews.utils.AppPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Ashwani Kumar Singh on 24,April,2023.
 */
class VideoCategoryViewModel(
    private val videoCategoryUseCase: VideoCategoryUseCase,
    private val updateVideoCategoriesUseCase: UpdateVideoCategoriesUseCase
) : ViewModel() {

    private val _videoCategorySuccessState = MutableStateFlow<UserVideoCategory?>(null)
    val videoCategorySuccessState: StateFlow<UserVideoCategory?> get() = _videoCategorySuccessState

    private val _updateCategoriesSuccessState = MutableStateFlow<UpdateCategories?>(null)
    val updateCategoriesSuccessState: StateFlow<UpdateCategories?> get() = _updateCategoriesSuccessState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> get() = _errorState

    private val _loadingState = MutableStateFlow(true)
    val loadingState: MutableStateFlow<Boolean> get() = _loadingState

    fun loadVideoCategory() {
        val languageString = AppPreference.getSelectedLanguagesAsString()
        videoCategoryUseCase.invoke(
            scope = viewModelScope,
            params = languageString,
            onResult = object :
                UseCaseResponse<VideoCategoryResult> {
                override fun onSuccess(result: VideoCategoryResult) {
                    val userVideoCategory = UserVideoCategory().mapper(
                        status = result.status,
                        videoCategories = result.data
                    )
                    _videoCategorySuccessState.value = userVideoCategory
                }

                override fun onError(apiError: ApiError) {
                    _errorState.value = "${apiError.getErrorMessage()}"
                }

                override fun onLoading(isLoading: Boolean) {
                    _loadingState.value = isLoading
                }

            })

    }

    fun updateCategoriesApi(categories: Any) {
        updateVideoCategoriesUseCase.invoke(
            scope = viewModelScope,
            params = categories,
            onResult = object : UseCaseResponse<UpdateCategories> {
                override fun onSuccess(type: UpdateCategories) {
                    _updateCategoriesSuccessState.value = type
                }

                override fun onError(apiError: ApiError) {
                    _errorState.value = "${apiError.getErrorMessage()}"
                }

                override fun onLoading(isLoading: Boolean) {
                    _loadingState.value = isLoading
                }

            })
    }

}