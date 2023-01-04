package com.earl.gpns.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.UserDomainToUiMapper
import com.earl.gpns.ui.models.UserUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CompanionGroupSettingsViewModel @Inject constructor(
    private val interactor: Interactor,
    private val userDomainToUiMapper: UserDomainToUiMapper<UserUi>
) : ViewModel() {

    private val companionsList = MutableStateFlow<List<UserUi>>(emptyList())
    val _companionsList = companionsList.asStateFlow()

    fun fetchAllCompanionsInGroup(token: String, groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchAllCompanionsInGroup(token, groupId).map { it.map(userDomainToUiMapper) }
            withContext(Dispatchers.Main) {
                companionsList.value = list
            }
        }
    }

    fun removeCompanionFromGroup(token: String, groupId: String, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.removeCompanionFromGroup(token, groupId, username)
            val removedUser = companionsList.value.find { it.provideName() == username }
            if (removedUser != null) {
                companionsList.value -= removedUser
            }
        }
    }

    fun leaveFromCompanionGroup(token: String, groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.leaveFromCompanionGroup(token, groupId)
        }
    }
}