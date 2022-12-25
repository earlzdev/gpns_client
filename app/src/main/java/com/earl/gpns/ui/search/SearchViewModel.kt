package com.earl.gpns.ui.search

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.mappers.TripFormDomainToUiMapper
import com.earl.gpns.ui.models.TripFormUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val interactor: Interactor,
    private val tripFormDomainToUiMapper: TripFormDomainToUiMapper<TripFormUi>
) : ViewModel() {

    private val tripForms: MutableStateFlow<List<TripFormUi>> = MutableStateFlow(emptyList())
    val _tripForms = tripForms.asStateFlow()
    private val searchTripFormsLiveData = MutableLiveData<List<TripFormUi>>()

    fun fetchAllTripForms(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = interactor.fetchAllTripForms(token).map { it.map(tripFormDomainToUiMapper) }
            withContext(Dispatchers.Main) {
                Log.d("tag", "fetchAllTripForms ui : $list")
                searchTripFormsLiveData.value = list
            }
        }
    }

    fun observeSearchTripFormsLiveData(owner: LifecycleOwner, observer: Observer<List<TripFormUi>>) {
        searchTripFormsLiveData.observe(owner, observer)
    }
}