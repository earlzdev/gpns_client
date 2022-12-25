package com.earl.gpns.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentSearchBinding
import com.earl.gpns.ui.SearchFormsDetails
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(), OnSearchFormClickListener {

    lateinit var viewModel: SearchViewModel

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        viewModel.fetchAllTripForms(preferenceManager.getString(Keys.KEY_JWT) ?: "")
        recycler()
        binding.newFormBtn.setOnClickListener {
            navigator.newSearchForm()
        }
    }

    private fun recycler() {
        val adapter = TripFormsRecyclerAdapter(this)
        binding.recyclerTripForms.adapter = adapter
        viewModel.observeSearchTripFormsLiveData(this) {
            adapter.submitList(it)
        }
    }

    override fun showDetails(role: String, details: SearchFormsDetails) {
        if (role == COMPANION_ROLE) {
            navigator.companionFormDetails(details)
        } else {
            navigator.driverFormDetails(details)
        }
        Log.d("tag", "showDetails: ${details.toString()}")
    }

    companion object {

        fun newInstance() = SearchFragment()
        private const val COMPANION_ROLE = "COMPANION_ROLE"
    }
}