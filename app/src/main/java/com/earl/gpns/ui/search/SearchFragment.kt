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
class SearchFragment : BaseFragment<FragmentSearchBinding>(), OnSearchFormClickListener, SearchingSocketService {

    private lateinit var viewModel: SearchViewModel
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
        binding.newFormBtn.setOnClickListener {
            navigator.newSearchForm()
        }
        binding.notificationIcon.setOnClickListener {
            navigator.tripNotifications()
            binding.newNotificationIcon.isVisible = false
        }
    }

    private fun recycler() {
        val adapter = TripFormsRecyclerAdapter(this)
        binding.recyclerTripForms.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel._tripForms
                .onEach {
                        forms ->
                    Log.d("tag", "recycler: new $forms")
                    adapter.submitList(forms)
                }
                .collect()
        }
    }

    override fun showDetails(role: String, details: SearchFormsDetails) {
        if (role == COMPANION_ROLE) {
            navigator.companionFormDetails(details)
        } else {
            navigator.driverFormDetails(details)
        }
    }

    private fun initSearchingSocket() {
        viewModel.initSearchingSocket(preferenceManager.getString(Keys.KEY_JWT) ?: "", this)
    }

    override fun showNotification(notification: TripNotificationDomain) {
        lifecycleScope.launch(Dispatchers.Main) {
//            val notificationUi = notification.mapToUi(tripNotificationDomainToUiMapper).provideDataForNotification()
//            Log.d("tag", "showNotification search fragment: ${notificationUi}")
            binding.newNotificationIcon.isVisible = true
            Toast.makeText(requireContext(), "NEW INVITE", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        fun newInstance() = SearchFragment()
        private const val COMPANION_ROLE = "COMPANION_ROLE"
    }
}