package com.earl.gpns.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.data.retrofit.requests.RegisterRequest
import com.earl.gpns.domain.Interactor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val interactor: Interactor
): ViewModel() {

    fun register(request: RegisterRequest, callback: AuthResultListener) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.register(request, callback)
        }
    }
}