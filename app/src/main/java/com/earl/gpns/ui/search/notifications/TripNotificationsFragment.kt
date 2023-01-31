package com.earl.gpns.ui.search.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.Keys
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
            onDestroy()
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
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.notificationsRecycler.layoutManager = linearLayoutManager
        viewModel.observeTripNotificationsLiveData(this) { list ->
            list.onEach {
//                viewModel.insertNotificationIdIntoDb(it.id)
            }
            recyclerAdapter.submitList(list)
        }
    }

    override fun showNotificationDetails(id: String, username: String, tripRole: String, watchable: Boolean) {
        if (watchable) {
//            viewModel.insertNotificationIdIntoDb(id)
            viewModel.fetchTripNotificationById(id)
            viewModel.observeExistedTripNotificationLiveData(this) {
                val notification = it.provideTripNotificationUiRecyclerItem()
                val viewRegime = if (notification.authorName == (preferenceManager.getString(Keys.KEY_NAME) ?: "")) {
                    OWN_INVITE
                } else {
                    NOTIFICATION
                }
                if (tripRole == COMPANION_ROLE) {
                    viewModel.fetchCompanionForm(
                        preferenceManager.getString(Keys.KEY_JWT) ?: "",
                        username
                    )
                    viewModel.observeCompanionFormLiveData(this) {
                        if (it != null) {
                            navigator.companionFormDetails(it.provideCompanionDetailsUi(), viewRegime, id)
                        }
                    }
                } else {
                    viewModel.fetchDriverForm(
                        preferenceManager.getString(Keys.KEY_JWT) ?: "",
                        username
                    )
                    viewModel.observeDriverFormLiveData(this) {
                        if (it != null) {
                            navigator.driverFormDetails(it.provideDriverFormDetailsUi(), viewRegime, id)
                        }
                    }
                }
            }
        } else {
//            viewModel.insertNotificationIdIntoDb(id)
        }
    }

    companion object {
        fun newInstance() = TripNotificationsFragment()
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val DRIVER_ROLE = "DRIVER_ROLE"
        private const val DETAILS = "DETAILS"
        private const val NOTIFICATION = "NOTIFICATION"
        private const val OWN_INVITE = "OWN_INVITE"
    }
}