package com.earl.gpns.ui.search

import androidx.lifecycle.ViewModel
import com.earl.gpns.domain.Interactor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val interactor: Interactor,

) : ViewModel() {


}