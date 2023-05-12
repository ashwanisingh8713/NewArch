package com.videopager.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SharedEventViewModel:ViewModel() {

    private var _cacheVideoUrl= MutableSharedFlow<Pair<String, String>>()
    val cacheVideoUrl = _cacheVideoUrl.asSharedFlow()

    fun cacheVideoData(uri: String, id: String) {
        viewModelScope.launch {
            _cacheVideoUrl.emit(Pair(uri, id))
        }
    }

    private var _userLoginStatus= MutableSharedFlow<Pair<Boolean, String>>()
    val cacheUserStatus = _userLoginStatus.asSharedFlow()

    fun sendUserPreferenceData(loginStatus: Boolean, userId: String) {
        viewModelScope.launch {
            delay(2000)
            _userLoginStatus.emit(Pair(loginStatus, userId))
        }
    }


    private var _launchLoginEvent= MutableSharedFlow<Boolean>()
    val getLoginEventStatus = _launchLoginEvent.asSharedFlow()

    fun launchLoginEvent(launchLogin: Boolean) {
        viewModelScope.launch {
            _launchLoginEvent.emit(launchLogin)
        }
    }

}