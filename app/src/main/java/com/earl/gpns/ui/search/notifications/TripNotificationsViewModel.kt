package com.earl.gpns.ui.search.notifications

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
import kotlinx.coroutines.async
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

    private val tripNotificationsLiveData = MutableLiveData<List<TripNotificationRecyclerItemUi>>()
    private val companionFormLiveData = MutableLiveData<CompanionFormUi?>()
    private val driverFormLiveData = MutableLiveData<DriverFormUi?>()
    private val existedTripNotificationLiveData = MutableLiveData<TripNotificationUi>()
    private val watchedNotificationsIdsListLivedData = MutableLiveData<String>()

    fun fetchNotifications(token: String, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val remoteList = interactor.fetchAllTripNotifications(token)
            val recyclerList = remoteList
                .map { it.mapToUi(tripNotificationDomainToUiMapper) }
                .map { it.provideTripNotificationUiRecyclerItem() }
            val watchedNotificationsIdsList = interactor.fetchAllWatchedNotificationsIds()
            withContext(Dispatchers.Main) {
                tripNotificationsLiveData.value = recyclerList.onEach {
                    if (watchedNotificationsIdsList.contains(it.id)) {
                        it.read = 1
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            interactor.insertNewWatchedNotificationId(it.id)
                        }
                    }
                }
            }
        }
    }

//    fun insertNotificationIdIntoDb(id: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val existedList = fetchAllWatchedNotificationsIdsList()
//            Log.d("tag", "insertNotificationIdIntoDb: existed list -> $existedList")
//            if (!existedList.contains(id)) {
//                Log.d("tag", "insertNotificationIdIntoDb: INSERTED NEW NOTIFICATION TRIPNOTIFICATIONVIEWIEWMODEL")
//                interactor.insertNewWatchedNotificationId(id)
//            }
//        }
//    }

//    private suspend fun fetchAllWatchedNotificationsIdsList() : List<String> {
//        return viewModelScope.async(Dispatchers.IO) {
//            val list = interactor.fetchAllWatchedNotificationsIds()
//            withContext(Dispatchers.Main) {
//                return@withContext list
//            }
//        }.await()
//    }

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

    fun fetchTripNotificationById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val notificationUi = interactor.fetchTripNotification(id).mapToUi(tripNotificationDomainToUiMapper)
            withContext(Dispatchers.Main) {
                existedTripNotificationLiveData.value = notificationUi
            }
        }
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

    fun observeExistedTripNotificationLiveData(owner: LifecycleOwner, observer: Observer<TripNotificationUi>) {
        existedTripNotificationLiveData.observe(owner, observer)
    }
}