package com.earl.gpns.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentSearchBinding
import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper
import com.earl.gpns.domain.models.TripNotificationDomain
import com.earl.gpns.domain.webSocketActions.RemoveDeletedSearchingFormFromList
import com.earl.gpns.domain.webSocketActions.services.SearchingSocketService
import com.earl.gpns.ui.SearchFormsDetails
import com.earl.gpns.ui.models.TripNotificationUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(), OnSearchFormClickListener, NotificationReactListener {

    private lateinit var viewModel: SearchViewModel
    private lateinit var recyclerAdapter: TripFormsRecyclerAdapter
    @Inject
    lateinit var tripNotificationDomainToUiMapper: TripNotificationDomainToUiMapper<TripNotificationUi>

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        initSearchingSocket()
        recycler()
        observeNewNotifications()
        initViews()
        binding.newFormBtn.setOnClickListener {
            navigator.newSearchForm()
        }
        binding.notificationIcon.setOnClickListener {
            navigator.tripNotifications()
            binding.newNotificationIcon.isVisible = false
        }
        binding.searchChapter.setOnClickListener {
            viewModel.clearTripNotificationDb()
        }
    }

    private fun initViews() {
        viewModel.observeUnwatchedNotificationsLiveData(this) {
            binding.newNotificationIcon.isVisible = it == NEW_NOTIFICATION
        }
    }

    private fun recycler() {
        recyclerAdapter = TripFormsRecyclerAdapter(this)
        binding.recyclerTripForms.adapter = recyclerAdapter
        lifecycleScope.launchWhenStarted {
            viewModel._tripForms
                .onEach {
                        forms ->
                    recyclerAdapter.submitList(forms)
                }
                .collect()
        }
    }

    override fun showDetails(role: String, details: SearchFormsDetails) {
        if (role == COMPANION_ROLE) {
            navigator.companionFormDetails(details, DETAILS, "")
        } else {
            navigator.driverFormDetails(details, DETAILS, "")
        }
    }

    private fun initSearchingSocket() {
        viewModel.initSearchingSocket(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
        preferenceManager.getString(Keys.KEY_NAME) ?: "",
            this
        )
    }

    private fun observeNewNotifications() {
        viewModel.observeNotificationLiveData(this) {
            Toast.makeText(requireContext(), "У Вас новое уведомление!", Toast.LENGTH_SHORT).show()
            binding.newNotificationIcon.isVisible = true
        }
    }

    override fun reactOnRemovingFromCompGroupNotification() {
        preferenceManager.putBoolean(Keys.IS_STILL_IN_COMP_GROUP, false)
    }

    override fun savePointThatUserIsStillInCompGroup() {
        preferenceManager.putBoolean(Keys.IS_STILL_IN_COMP_GROUP, true)
    }

    companion object {

        fun newInstance() = SearchFragment()
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val DETAILS = "DETAILS"
        private const val NOTIFICATION = "NOTIFICATION"
        private const val NEW_NOTIFICATION = 1
    }
}