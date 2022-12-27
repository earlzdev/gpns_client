package com.earl.gpns.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentNotificationsBinding
import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper
import com.earl.gpns.domain.models.TripNotificationDomain
import com.earl.gpns.domain.webSocketActions.NewTripInviteNotification
import com.earl.gpns.ui.models.TripNotificationUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
    }

    private fun recycler() {
        recyclerAdapter = TripNotificationsRecyclerAdapter(this)
        binding.notificationsRecycler.adapter = recyclerAdapter
        viewModel.fetchAllTripNotificationsFromLocalDb()
        viewModel.fetchNotifications(preferenceManager.getString(Keys.KEY_JWT) ?: "")
        val existedList = viewModel.provideExistedTripNotificationsLiveData()
        val existedNotificationsIdsList = existedList.map { it.provideId() }
        viewModel.observeTripNotificationsLiveData(this) { list ->
            list.onEach { notification ->
                if (existedNotificationsIdsList.contains(notification.id)) {
                    notification.read = 1
                }
            }
            recyclerAdapter.submitList(list)
        }
    }

    override fun showNotificationDetails(id: String) {

    }

    companion object {
        fun newInstance() = TripNotificationsFragment()
    }
}