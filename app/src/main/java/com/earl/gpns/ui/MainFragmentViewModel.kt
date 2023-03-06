package com.earl.gpns.ui

import androidx.lifecycle.*
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.UserDomainToUiMapper
import com.earl.gpns.ui.models.UserUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val interactor: Interactor,
    private val userDomainToUiMapper: UserDomainToUiMapper<UserUi>
) : ViewModel() {

    private val userInfoLiveData = MutableLiveData<UserUi>()

    fun fetchUserInfo(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val info = interactor.fetchUserInfo(token)?.map(userDomainToUiMapper)
            withContext(Dispatchers.Main) {
                if (info != null) {
                    userInfoLiveData.value = info
                }
            }
        }
    }

    fun observeUserInfoLiveData(owner: LifecycleOwner, observer: Observer<UserUi>) {
        userInfoLiveData.observe(owner, observer)
    }
}