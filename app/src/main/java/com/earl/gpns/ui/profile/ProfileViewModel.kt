package com.earl.gpns.ui.profile

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.CompanionFormDomainToUiMapper
import com.earl.gpns.domain.mappers.DriverFormDomainToUiMapper
import com.earl.gpns.ui.models.CompanionFormUi
import com.earl.gpns.ui.models.DriverFormUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: Interactor,
    private val companionFormDomainToUiMapper: CompanionFormDomainToUiMapper<CompanionFormUi>,
    private val driverFormDomainToUiMapper: DriverFormDomainToUiMapper<DriverFormUi>
) : ViewModel() {

    private val companionTripFormLiveData = MutableLiveData<CompanionFormUi>()
    private val driverTripFormLiveData = MutableLiveData<DriverFormUi>()

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.closeMessagingSocket()
            interactor.closeChatSocketSession()
            interactor.clearDatabase()
        }
    }

    fun deleteDriverFormForm(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.clearWatchedNotificationsDb()
            interactor.clearNotificationsDb()
            interactor.deleteDriverForm(token)
            interactor.clearLocalDbCompanionGroupUsersList()
        }
    }

    fun deleteCompanionForm(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.clearWatchedNotificationsDb()
            interactor.clearNotificationsDb()
            interactor.deleteCompanionForm(token)
            interactor.clearLocalDbCompanionGroupUsersList()
        }
    }

    fun fetchCompanionFormFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val tripForm = interactor.fetchCompanionTripFormFromLocalDb()
            withContext(Dispatchers.Main) {
                companionTripFormLiveData.value = tripForm.mapToUi(companionFormDomainToUiMapper)
            }
        }
    }

    fun fetchDriverFormFromLocalDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val tripForm = interactor.fetchDriverTripFormFromLocalDb()
            withContext(Dispatchers.Main) {
                driverTripFormLiveData.value = tripForm.mapToUi(driverFormDomainToUiMapper)
            }
        }
    }

    fun observeDriverTripFormLiveData(owner: LifecycleOwner, observer: Observer<DriverFormUi>) {
        driverTripFormLiveData.observe(owner, observer)
    }

    fun observeCompanionTripFormLiveData(owner: LifecycleOwner, observer: Observer<CompanionFormUi>) {
        companionTripFormLiveData.observe(owner, observer)
    }
}