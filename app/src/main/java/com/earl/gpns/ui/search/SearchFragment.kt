package com.earl.gpns.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.earl.gpns.R
import com.earl.gpns.databinding.FragmentSearchBinding
import com.earl.gpns.domain.mappers.TripNotificationDomainToUiMapper
import com.earl.gpns.ui.SearchFormsDetails
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.BitmapFromStringDecoder
import com.earl.gpns.ui.core.Keys
import com.earl.gpns.ui.models.TripNotificationUi
import dagger.hilt.android.AndroidEntryPoint
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
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ownTripForm.onEach { tripForm ->
                    val details = tripForm?.provideTripCardDetails()
                    if (details != null) {
                        binding.usersOwnSearchForm.visibility = View.VISIBLE
                        binding.searchCard.userName.text = details.name
                        if (details.image.isNotEmpty()) {
                            binding.searchCard.userAvatar.setImageBitmap(BitmapFromStringDecoder().decode(details.image))
                        }
                        if (details.tripRole == COMPANION_ROLE) {
                            binding.searchCard.companionIcon.isVisible = true
                            binding.searchCard.driverIcon.isVisible = false
                            binding.searchCard.tripRole.text = requireContext().resources.getString(R.string.compa)
                        } else {
                            binding.searchCard.companionIcon.isVisible = false
                            binding.searchCard.driverIcon.isVisible = true
                            binding.searchCard.tripRole.text = requireContext().resources.getString(R.string.driv)
                        }
                        binding.searchCard.fromEd.text = details.fromTv
                        binding.searchCard.driveToEd.text = details.toTv
                        binding.searchCard.schduleEd.text = details.scheduleTv
                        binding.searchCard.lookTripDetailsBtn.setOnClickListener {
                            if (tripForm.provideTripRole() == COMPANION_ROLE) {
                                navigator.companionFormDetails(tripForm.provideDetails(), OWN_TRIP_FORM, "")
                            } else {
                                navigator.driverFormDetails(tripForm.provideDetails(), OWN_TRIP_FORM, "")
                            }
                        }
                    } else {
                        binding.usersOwnSearchForm.visibility = View.GONE
                    }
                }.collect()
            }
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
            Toast.makeText(requireContext(), getString(R.string.new_notification), Toast.LENGTH_SHORT).show()
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
        private const val NEW_NOTIFICATION = 1
        private const val OWN_TRIP_FORM = "OWN_TRIP_FORM"
    }
}