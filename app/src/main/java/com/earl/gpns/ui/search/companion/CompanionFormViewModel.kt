package com.earl.gpns.ui.search.companion

import android.content.Context
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earl.gpns.R
import com.earl.gpns.domain.Interactor
import com.earl.gpns.domain.models.CompanionFormDomain
import com.earl.gpns.domain.models.TripNotificationDomain
import com.earl.gpns.ui.mappers.CompanionFormUiToDomainMapper
import com.earl.gpns.ui.mappers.TripNotificationUiToDomainMapper
import com.earl.gpns.ui.models.CompanionFormUi
import com.earl.gpns.ui.models.TripNotificationUi
import com.earl.gpns.ui.search.SpinnerInterfaceInitializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanionFormViewModel @Inject constructor(
    private val interactor: Interactor,
    private val companionFormUiToDomainMapper: CompanionFormUiToDomainMapper<CompanionFormDomain>,
    private val tripNotificationUiToDomainMapper: TripNotificationUiToDomainMapper<TripNotificationDomain>
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

    fun sendNewCompanionForm(token: String, form: CompanionFormUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.sendNewCompanionForm(token, form.mapToDomain(companionFormUiToDomainMapper))
        }
    }

    fun inviteCompanion(token: String, notification: TripNotificationUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.inviteCompanion(
                token,
                notification.mapToDomain(tripNotificationUiToDomainMapper)
            )
        }
    }
}