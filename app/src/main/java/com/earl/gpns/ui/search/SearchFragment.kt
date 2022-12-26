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
import com.earl.gpns.databinding.FragmentSearchBinding
import com.earl.gpns.domain.webSocketActions.services.SearchingSocketService
import com.earl.gpns.ui.SearchFormsDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(), OnSearchFormClickListener, SearchingSocketService {

    lateinit var viewModel: SearchViewModel

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
    }

    private fun recycler() {
        val adapter = TripFormsRecyclerAdapter(this)
        binding.recyclerTripForms.adapter = adapter
//        viewModel.observeSearchTripFormsLiveData(this) {
//            adapter.submitList(it)
//        }
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

    companion object {

        fun newInstance() = SearchFragment()
        private const val COMPANION_ROLE = "COMPANION_ROLE"
    }
}