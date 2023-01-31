package com.earl.gpns.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.domain.AuthResultListener
import com.earl.gpns.data.models.remote.requests.LoginRequest
import com.earl.gpns.domain.Interactor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val interactor: Interactor
) : ViewModel() {

    fun login(request: LoginRequest, callback: AuthResultListener) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.login(request, callback)
        }
    }
}