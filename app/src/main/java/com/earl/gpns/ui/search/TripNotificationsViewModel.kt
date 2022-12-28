package com.earl.gpns.ui.search

import androidx.lifecycle.*
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.CompanionFormDomainToUiMapper
import com.earl.gpns.domain.mappers.DriverFormDomainToUiMapper
import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper
import com.earl.gpns.domain.models.TripNotificationDomain
import com.earl.gpns.ui.mappers.TripNotificationUiToDomainMapper
import com.earl.gpns.ui.models.CompanionFormUi
import com.earl.gpns.ui.models.DriverFormUi
import com.earl.gpns.ui.models.TripNotificationRecyclerItemUi
import com.earl.gpns.ui.models.TripNotificationUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TripNotificationsViewModel @Inject constructor(
    private val interactor: Interactor,
    private val tripNotificationDomainToUiMapper: TripNotificationDomainToUiMapper<TripNotificationUi>,
    private val tripNotificationUiToDomainMapper: TripNotificationUiToDomainMapper<TripNotificationDomain>,
    private val companionFormDomainToUiMapper: CompanionFormDomainToUiMapper<CompanionFormUi>,
    private val driverFormDomainToUiMapper: DriverFormDomainToUiMapper<DriverFormUi>
) : ViewModel() {

    private val existedTripNotificationsLiveData = MutableLiveData<List<TripNotificationUi>>()
    private val tripNotificationsLiveData = MutableLiveData<List<TripNotificationRecyclerItemUi>>()
    private val companionFormLiveData = MutableLiveData<CompanionFormUi?>()
    private val driverFormLiveData = MutableLiveData<DriverFormUi?>()
    private var fetchedList = mutableListOf<TripNotificationUi>()

    fun fetchAllTripNotificationsFromLocalDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchAllTripNotificationFromLocalDb().map { it.mapToUi(tripNotificationDomainToUiMapper) }
            withContext(Dispatchers.Main) {
                existedTripNotificationsLiveData.value = list
            }
        }
    }

    fun fetchNotifications(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchAllTripNotifications(token).map { it.mapToUi(tripNotificationDomainToUiMapper) }
            withContext(Dispatchers.Main) {
                tripNotificationsLiveData.value = list.map { it.provideTripNotificationUiRecyclerItem() }
                fetchedList = list.toMutableList()
            }
        }
    }

    fun insertNotificationIntoDb(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val wathcedNotification = fetchedList.find { it.provideId() == id }
            if (wathcedNotification != null) {
                interactor.insertNewNotificationIntoDb(wathcedNotification.mapToDomain(tripNotificationUiToDomainMapper))
            }
        }
    }

    fun fetchCompanionForm(token: String, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val companionForm = interactor.fetchCompanionForm(token, username)?.mapToUi(companionFormDomainToUiMapper)
            withContext(Dispatchers.Main) {
                companionFormLiveData.value = companionForm
            }
        }
    }

    fun fetchDriverForm(token: String, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val driverForm = interactor.fetchDriverForm(token, username)?.mapToUi(driverFormDomainToUiMapper)
            withContext(Dispatchers.Main) {
                driverFormLiveData.value = driverForm
            }
        }
    }

    fun provideExistedTripNotificationsLiveData() : List<TripNotificationUi> {
        return existedTripNotificationsLiveData.value ?: emptyList()
    }

    fun observeTripNotificationsLiveData(owner: LifecycleOwner, observer: Observer<List<TripNotificationRecyclerItemUi>>) {
        tripNotificationsLiveData.observe(owner, observer)
    }

    fun observeCompanionFormLiveData(owner: LifecycleOwner, observer: Observer<CompanionFormUi?>) {
        companionFormLiveData.observe(owner, observer)
    }

    fun observeDriverFormLiveData(owner: LifecycleOwner, observer: Observer<DriverFormUi?>) {
        driverFormLiveData.observe(owner, observer)
    }
}