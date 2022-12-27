package com.earl.gpns.ui.search

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper
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
    private val tripNotificationDomainToUiMapper: TripNotificationDomainToUiMapper<TripNotificationUi>
) : ViewModel() {

    private val existedTripNotificationsLiveData = MutableLiveData<List<TripNotificationUi>>()
    private val tripNotificationsLiveData = MutableLiveData<List<TripNotificationRecyclerItemUi>>()

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
            }
        }
    }

    fun insertNotificationIntoDb(id: String) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun provideExistedTripNotificationsLiveData() : List<TripNotificationUi> {
        return existedTripNotificationsLiveData.value ?: emptyList()
    }

    fun observeTripNotificationsLiveData(owner: LifecycleOwner, observer: Observer<List<TripNotificationRecyclerItemUi>>) {
        tripNotificationsLiveData.observe(owner, observer)
    }
}