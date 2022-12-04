package com.earl.gpns.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.domain.Interactor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: Interactor,
) : ViewModel() {

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.closeMessagingSocket()
            interactor.closeChatSocketSession()
            interactor.clearDatabase()
        }
    }
}