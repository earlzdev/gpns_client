package com.earl.gpns.ui.search

import android.util.Log
import androidx.lifecycle.*
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.TripFormDomainToUiMapper
import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper
import com.earl.gpns.domain.models.TripNotificationDomain
import com.earl.gpns.domain.webSocketActions.services.SearchingSocketService
import com.earl.gpns.ui.mappers.TripNotificationUiToDomainMapper
import com.earl.gpns.ui.models.TripFormUi
import com.earl.gpns.ui.models.TripNotificationUi
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
class SearchViewModel @Inject constructor(
    private val interactor: Interactor,
    private val tripFormDomainToUiMapper: TripFormDomainToUiMapper<TripFormUi>,
    private val tripNotificationUiToDomainMapper: TripNotificationUiToDomainMapper<TripNotificationDomain>,
    private val tripNotificationDomainToUiMapper: TripNotificationDomainToUiMapper<TripNotificationUi>
) : ViewModel(), SearchingSocketService {

    private val tripForms: MutableStateFlow<List<TripFormUi>> = MutableStateFlow(emptyList())
    val _tripForms = tripForms.asStateFlow()
    private val searchTripFormsLiveData = MutableLiveData<List<TripFormUi>>()
    private val newNotificationsLiveData = MutableLiveData<Int>()
    private val remoteNotificationsList = MutableLiveData<List<TripNotificationUi>>()
    private val localDbNotificationsList = MutableLiveData<List<TripNotificationUi>>()
    private val newUnwatchedNotificationsLiveData = MutableLiveData<Int>()
    private val remoteNotificationsListLiveData = MutableLiveData<List<TripNotificationUi>>()
    private var username: String? = null

    fun initSearchingSocket(token: String, name: String) {
        fetchAllTripForms(token)
        fetchAllNotifications(token)
        username = name
        viewModelScope.launch(Dispatchers.IO) {
            when (interactor.initSearchingSocket(token)) {
                true -> {
                    interactor.observeSearchingForms(this@SearchViewModel).onEach { newForm ->
                        if (newForm != null) {
                            tripForms.value += newForm.map(tripFormDomainToUiMapper)
                        }
                    }.collect()
                }
                else -> {
                    Log.d("tag", "initSearchingSocket: error")
                }
            }
        }
    }

    private fun fetchAllNotifications(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val remoteNotificationsList = interactor.fetchAllTripNotifications(token).map { it.mapToUi(tripNotificationDomainToUiMapper) }
            val remoteNotificationsIdsList = remoteNotificationsList.map { it.provideId() }
            Log.d("tag", "fetchAllNotifications: remote -> ${remoteNotificationsIdsList}")
            val localDbNotificationsList = interactor.fetchAllTripNotificationFromLocalDb().map { it.mapToUi(tripNotificationDomainToUiMapper) }
            val localDbNotificationsIdsList = localDbNotificationsList.map { it.provideId() }
            Log.d("tag", "fetchAllNotifications: local -> ${localDbNotificationsIdsList}")
            val watchedNotificationsIdsList = interactor.fetchAllWatchedNotificationsIds()
            Log.d("tag", "fetchAllNotifications: local ids -> ${watchedNotificationsIdsList}")
            val newNotificationsList = mutableListOf<TripNotificationUi>()
            for (i in remoteNotificationsIdsList) {
                if (localDbNotificationsList.find { it.provideId() == i } == null) {
                    newNotificationsList.add(remoteNotificationsList.find { it.provideId() == i }!!)
                } else if (!watchedNotificationsIdsList.contains(i)
                    && remoteNotificationsList.find { it.provideId() == i }!!.provideAuthorName() == username) {
                    interactor.insertNewWatchedNotificationId(remoteNotificationsList.find { it.provideId() == i }?.provideId()!!)
                }
            }
            withContext(Dispatchers.Main) {
                if (remoteNotificationsIdsList.size != watchedNotificationsIdsList.size) {
                    newUnwatchedNotificationsLiveData.value = NEW_NOTIFICATION
                }
                if (newNotificationsList.isNotEmpty()) {
                    newNotificationsList.forEach {
                        reactOnNewNotification(it.mapToDomain(tripNotificationUiToDomainMapper))
                    }
                }
            }
        }
    }

    override fun updateNotificationsList() {

    }

    override fun reactOnNewNotification(notification: TripNotificationDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertNewNotificationIntoDb(notification)
            val watchedNotificationsList = interactor.fetchAllWatchedNotificationsIds()
            val newNotification = notification.mapToUi(tripNotificationDomainToUiMapper).provideTripNotificationUiRecyclerItem()
            if (!watchedNotificationsList.contains(newNotification.id) && newNotification.authorName == username) {
                Log.d("tag", "reactOnNewNotification: insrted")
                interactor.insertNewWatchedNotificationId(newNotification.id)
            } else {
                Log.d("tag", "NEW NOTIFICATIONS")
                withContext(Dispatchers.Main) {
                    newNotificationsLiveData.value = NEW_NOTIFICATION
                }
            }
            Log.d("tag", "reactOnNewNotification: reacted on new notification $notification")
        }
    }

    override fun removeDeletedSearchingFormFromList(username: String) {
        viewModelScope.launch(Dispatchers.Main) {
            removeDeletedSearchingForm(username)
        }
    }

    private fun fetchAllTripForms(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchAllTripForms(token).map { it.map(tripFormDomainToUiMapper) }
            withContext(Dispatchers.Main) {
                Log.d("tag", "fetchAllTripForms ui : $list")
                tripForms.value += list
            }
        }
    }

    private fun removeDeletedSearchingForm(username: String) {
        viewModelScope.launch(Dispatchers.Main) {
            if (tripForms.value.isNotEmpty()) {
                val form = tripForms.value.find { it.sameUsername(username) }!!
                tripForms.value -= form
            }
        }
    }

    fun clearTripNotificationDb() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.clearNotificationsDb()
            interactor.clearWatchedNotificationsDb()
        }
    }

    fun observeNotificationLiveData(owner: LifecycleOwner, observer: Observer<Int>) {
        newNotificationsLiveData.observe(owner, observer)
    }

    fun observeSearchTripFormsLiveData(owner: LifecycleOwner, observer: Observer<List<TripFormUi>>) {
        searchTripFormsLiveData.observe(owner, observer)
    }

    fun observeUnwatchedNotificationsLiveData(owner: LifecycleOwner, observer: Observer<Int>) {
        newUnwatchedNotificationsLiveData.observe(owner, observer)
    }

    companion object {
        private const val NEW_NOTIFICATION = 1
    }
}