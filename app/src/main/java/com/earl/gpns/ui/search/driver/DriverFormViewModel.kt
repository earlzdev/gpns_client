package com.earl.gpns.ui.search.driver

import android.content.Context
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.R
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.models.DriverFormDomain
import com.earl.gpns.ui.mappers.DriverFormUiToDomainMapper
import com.earl.gpns.ui.models.DriverFormUi
import com.earl.gpns.ui.search.SpinnerInterfaceInitializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverFormViewModel @Inject constructor(
    private val interactor: Interactor,
    private val driverFormUiToDomainMapper: DriverFormUiToDomainMapper<DriverFormDomain>
): ViewModel(), SpinnerInterfaceInitializer {

    override fun initSpinnerAdapter(
        textResource: Int,
        context: Context
    ): ArrayAdapter<CharSequence> {
        return ArrayAdapter.createFromResource(
            context,
            textResource,
            R.layout.custom_spinner_item
        ).also { adapter -> adapter.setDropDownViewResource(R.layout.custom_spinner_drop_down) }
    }

    fun sendNewDriverForm(token: String, driverForm: DriverFormUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.sendNewDriverForm(token, driverForm.mapToDomain(driverFormUiToDomainMapper))
        }
    }
}