package com.ns.news.presentation.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ns.news.data.db.Section
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SectionTypeSharedViewModel : ViewModel() {

  private val _drawerSharedViewModel = MutableSharedFlow<List<Section>>() // 1
  val drawerSharedViewModel = _drawerSharedViewModel.asSharedFlow() // 2

  private val _breadcrumbSharedViewModel = MutableSharedFlow<List<Section>>() // 3
  val breadcrumbSharedViewModel = _breadcrumbSharedViewModel.asSharedFlow() // 4

  fun setDrawerSections(drawerSections: List<Section>) { //5
    viewModelScope.launch {
      _drawerSharedViewModel.emit(drawerSections)
    }
  }

  fun setBreadcrumbSections(breadcrumbSections: List<Section>) { //6
    viewModelScope.launch {
      _breadcrumbSharedViewModel.emit(breadcrumbSections)
    }

  }
}

