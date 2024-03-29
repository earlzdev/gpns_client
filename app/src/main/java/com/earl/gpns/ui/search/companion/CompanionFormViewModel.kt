package com.earl.gpns.ui.search.companion

import android.content.Context
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.R
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper
import com.earl.gpns.domain.models.CompanionFormDomain
import com.earl.gpns.domain.models.TripNotificationDomain
import com.earl.gpns.ui.mappers.CompanionFormUiToDomainMapper
import com.earl.gpns.ui.mappers.TripNotificationUiToDomainMapper
import com.earl.gpns.ui.models.CompanionFormUi
import com.earl.gpns.ui.models.TripNotificationUi
import com.earl.gpns.ui.search.SpinnerInitializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CompanionFormViewModel @Inject constructor(
    private val interactor: Interactor,
    private val companionFormUiToDomainMapper: CompanionFormUiToDomainMapper<CompanionFormDomain>,
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

    fun sendNewCompanionForm(token: String, form: CompanionFormUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.sendNewCompanionForm(token, form.mapToDomain(companionFormUiToDomainMapper))
        }
    }

    fun inviteCompanion(token: String, notification: TripNotificationUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.inviteCompanion(
                token,
                notification.mapToDomain(tripNotificationUiToDomainMapper)
            )
        }
        insertNewNotificationIntoDb(notification)
    }

    private fun insertNewNotificationIntoDb(notificationUi: TripNotificationUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertNewNotificationIntoDb(notificationUi.mapToDomain(tripNotificationUiToDomainMapper))
            val existedList = interactor.fetchAllWatchedNotificationsIds()
            if (!existedList.contains(notificationUi.provideId())) {
                interactor.insertNewWatchedNotificationId(notificationUi.provideId())
            }
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

    fun insertNewUserIntoLocalDbCompGroup(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertNewUserIntoCompanionGroup(username)
        }
    }

    fun provideTripNotificationsLiveData() = tripNotificationsLiveData.value

    fun provideCompanionUsersListLiveDataValue() = companionGroupUsersLiveData.value

    fun fetchUsersListInCompanionGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchAllUsernamesFromCompanionGroupFromLocalDb()
            withContext(Dispatchers.Main) {
                companionGroupUsersLiveData.value = list
            }
        }
    }

    fun acceptCompanionToRideTogether(token: String, companionUsername: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.acceptCompanionToRideTogether(token, companionUsername)
        }
    }

    fun denyCompanionToRideTogether(token: String, companionUsername: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.denyCompanionToRideTogether(token, companionUsername)
        }
    }

    fun markTripNotificationAsNotActive(token: String, notificationId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.markTripNotificationAsNotActive(token, notificationId)
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

    fun clearTripFormInLocalDb() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.clearDriverFormInLocalDb()
        }
    }

    fun insertNewCompanionTripFormIntoLocalDb(form: CompanionFormUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.saveCompanionTripFormIntoLocalDb(form.mapToDomain(companionFormUiToDomainMapper))
        }
    }
}