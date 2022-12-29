package com.earl.gpns.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentNotificationsBinding
import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper
import com.earl.gpns.ui.models.TripNotificationUi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TripNotificationsFragment : BaseFragment<FragmentNotificationsBinding>(), NotificationOnClickListener {

    private lateinit var viewModel: TripNotificationsViewModel
    private lateinit var recyclerAdapter: TripNotificationsRecyclerAdapter
    @Inject
    lateinit var tripNotificationDomainToUiMapper: TripNotificationDomainToUiMapper<TripNotificationUi>

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNotificationsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TripNotificationsViewModel::class.java]
        recycler()
        viewModel.fetchNotifications(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            preferenceManager.getString(Keys.KEY_NAME) ?: ""
        )
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
    }

    private fun recycler() {
        recyclerAdapter = TripNotificationsRecyclerAdapter(
            preferenceManager.getString(Keys.KEY_NAME) ?: "",
            if (preferenceManager.getBoolean(Keys.IS_DRIVER)) DRIVER_ROLE else COMPANION_ROLE,
            this
        )
        binding.notificationsRecycler.adapter = recyclerAdapter
//        viewModel.fetchAllTripNotificationsFromLocalDb()
//        viewModel.fetchNotifications(preferenceManager.getString(Keys.KEY_JWT) ?: "")
//        val existedList = viewModel.provideExistedTripNotificationsLiveData()
//        val existedNotificationsIdsList = existedList.map { it.provideId() }
        viewModel.observeTripNotificationsLiveData(this) { list ->
//            list.onEach { notification ->
//                if (existedNotificationsIdsList.contains(notification.id)) {
//                    notification.read = 1
//                }
//            }
            recyclerAdapter.submitList(list)
        }
    }

    override fun showNotificationDetails(id: String, username: String, tripRole: String) {
        viewModel.insertNotificationIdIntoDb(id)
        if (tripRole == COMPANION_ROLE) {
            viewModel.fetchCompanionForm(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                username
            )
            viewModel.observeCompanionFormLiveData(this) {
                if (it != null) {
                    navigator.companionFormDetails(it.provideCompanionDetailsUi(), NOTIFICATION)
                }
            }
        } else {
            viewModel.fetchDriverForm(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                username)
            viewModel.observeDriverFormLiveData(this) {
                if (it != null) {
                    navigator.driverFormDetails(it.provideDriverFormDetailsUi(), NOTIFICATION)
                }
            }
        }
    }

    companion object {
        fun newInstance() = TripNotificationsFragment()
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val DRIVER_ROLE = "DRIVER_ROLE"
        private const val DETAILS = "DETAILS"
        private const val NOTIFICATION = "NOTIFICATION"
    }
}