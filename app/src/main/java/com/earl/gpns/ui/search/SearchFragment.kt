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
        binding.newFormBtn.setOnClickListener {
            navigator.newSearchForm()
        }
        binding.notificationIcon.setOnClickListener {
            navigator.tripNotifications()
            binding.newNotificationIcon.isVisible = false
        }
        binding.searchChapter.setOnClickListener {
            preferenceManager.putBoolean(Keys.HAS_SEARCH_FORM, false)
            preferenceManager.putBoolean(Keys.IS_DRIVER, false)
            Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show()
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
            navigator.companionFormDetails(details, DETAILS)
        } else {
            navigator.driverFormDetails(details, DETAILS)
        }
    }

    private fun initSearchingSocket() {
        viewModel.initSearchingSocket(preferenceManager.getString(Keys.KEY_JWT) ?: "", this)
    }

    override fun showNotification(notification: TripNotificationDomain) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.newNotificationIcon.isVisible = true
            Toast.makeText(requireContext(), "Кто-то пригласил Вас ездить вместе, проверьте уведомления", Toast.LENGTH_LONG).show()
        }
    }

    override fun removeDeletedSearchingFormFromList(username: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (recyclerAdapter.currentList.isNotEmpty()) {
                viewModel.removeDeletedSearchingForm(username)
            }
        }
    }

    companion object {

        fun newInstance() = SearchFragment()
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val DETAILS = "DETAILS"
        private const val NOTIFICATION = "NOTIFICATION"
    }
}