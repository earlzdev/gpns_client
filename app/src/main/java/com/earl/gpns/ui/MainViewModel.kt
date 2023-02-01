package com.earl.gpns.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.domain.Interactor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: Interactor
) : ViewModel() {

    fun closeSocketSession() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.closeChatSocketSession()
        }
    }
}