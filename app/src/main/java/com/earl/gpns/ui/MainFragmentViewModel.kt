package com.earl.gpns.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.core.OperationResultListener
import com.earl.gpns.domain.Interactor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val interactor: Interactor
) : ViewModel() {

    fun authenticate(token: String, callback: AuthResultListener) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.authenticate(token, callback)
        }
    }

    fun getSecretInfo(token: String, callback: OperationResultListener) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getSecretInfo(token, callback)
        }
    }
}