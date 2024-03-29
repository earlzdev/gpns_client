package com.earl.gpns.ui.search.driver

import android.content.Context
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.R
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper
import com.earl.gpns.domain.models.DriverFormDomain
import com.earl.gpns.domain.models.TripNotificationDomain
import com.earl.gpns.ui.mappers.DriverFormUiToDomainMapper
import com.earl.gpns.ui.mappers.TripNotificationUiToDomainMapper
import com.earl.gpns.ui.models.DriverFormUi
import com.earl.gpns.ui.models.TripNotificationUi
import com.earl.gpns.ui.search.SpinnerInitializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DriverFormViewModel @Inject constructor(
    private val interactor: Interactor,
    private val driverFormUiToDomainMapper: DriverFormUiToDomainMapper<DriverFormDomain>,
    private val tripNotificationUiToDomainMapper: TripNotificationUiToDomainMapper<TripNotificationDomain>,
    private val tripNotificationDomainToUiMapper: TripNotificationDomainToUiMapper<TripNotificationUi>
): ViewModel(), SpinnerInitializer {

    private val tripNotificationsLiveData = MutableLiveData<List<TripNotificationUi>>()
    private val companionGroupUsersLiveData = MutableLiveData<List<String>>()

    override fun initSpinnerAdapter(
        textResource: Int,
        context: Context
    ): ArrayAdapter<CharSequence> {
        return ArrayAdapter.createFromResource(
            context,
            textResource,
            R.layout.custom_spinner_item
        ).also { adapter -> adapter.setDropDownViewResource(R.layout.custom_spinner_drop_down) }
    }

    fun sendNewDriverForm(token: String, driverForm: DriverFormUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.sendNewDriverForm(token, driverForm.mapToDomain(driverFormUiToDomainMapper))
        }
    }

    fun inviteDriver(token: String, notificationUi: TripNotificationUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.inviteDriver(token, notificationUi.mapToDomain(tripNotificationUiToDomainMapper))
        }
        insertNewNotificationIntoDb(notificationUi)
    }

    private fun insertNewNotificationIntoDb(notificationUi: TripNotificationUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertNewNotificationIntoDb(notificationUi.mapToDomain(tripNotificationUiToDomainMapper))
        }
    }

    fun insertNewUserIntoLocalDbCompGroup(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertNewUserIntoCompanionGroup(username)
        }
    }

    fun fetchExistedNotificationsFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchAllTripNotificationFromLocalDb().map { it.mapToUi(tripNotificationDomainToUiMapper) }
            withContext(Dispatchers.Main) {
                tripNotificationsLiveData.value = list
            }
        }
    }

    fun provideCompanionUsersListLiveDataValue() = companionGroupUsersLiveData.value

    fun fetchUsersListInCompanionGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchAllUsernamesFromCompanionGroupFromLocalDb()
            withContext(Dispatchers.Main) {
                companionGroupUsersLiveData.value = list
            }
        }
    }

    fun provideExistedNotificationsListLiveData() = tripNotificationsLiveData.value

    fun acceptDriverToRideTogether(token: String, driverUsername: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.acceptDriverToRideTogether(token, driverUsername)
        }
    }

    fun denyDriverToRideTogether(token: String, driverUsername: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.denyDriverToRideTogether(token, driverUsername)
        }
    }

    fun markTripNotificationAsNotActive(token: String, notificationId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.markTripNotificationAsNotActive(token, notificationId)
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

    fun clearTripFormInLocalDb() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.clearDriverFormInLocalDb()
        }
    }

    fun insertDriverTripFormIntoLocalDb(form: DriverFormUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.saveDriverTripFormIntoLocalDb(form.mapToDomain(driverFormUiToDomainMapper))
        }
    }
}