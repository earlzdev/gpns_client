package com.earl.gpns.ui.usersFragment

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.BaseNewRoomUiToDomainMapper
import com.earl.gpns.domain.mappers.RoomDomainToUiMapper
import com.earl.gpns.domain.mappers.UserDomainToUiMapper
import com.earl.gpns.domain.models.NewRoomDtoDomain
import com.earl.gpns.ui.mappers.NewRoomUiToDomainMapper
import com.earl.gpns.ui.models.NewRoomDtoUi
import com.earl.gpns.ui.models.RoomUi
import com.earl.gpns.ui.models.UserUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val interactor: Interactor,
    private val userDomainToUiMapper: UserDomainToUiMapper<UserUi>,
    private val roomDomainToUiMapper: RoomDomainToUiMapper<RoomUi>,
) : ViewModel() {

    private val usersListLiveData = MutableLiveData<List<UserUi>>()
    private val existedRoomsListLiveData = MutableLiveData<List<RoomUi>>()

    fun fetchUsers(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchUsers(token)
            Log.d("tag", "fetchUsers: $list")
            withContext(Dispatchers.Main) {
                usersListLiveData.value = list.map { it.map(userDomainToUiMapper) }
            }
        }
        fetchExistedRoomsFromLocalDatabase()
    }

    private fun fetchExistedRoomsFromLocalDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchRoomsListFromLocalDb().map { it.map(roomDomainToUiMapper) }
            withContext(Dispatchers.Main) {
                existedRoomsListLiveData.value = list
            }
        }
    }

    fun observeUsersListLiveData(owner: LifecycleOwner, observer: Observer<List<UserUi>>) {
        usersListLiveData.observe(owner, observer)
    }

    fun provideExistedRoomsList() = existedRoomsListLiveData.value
}