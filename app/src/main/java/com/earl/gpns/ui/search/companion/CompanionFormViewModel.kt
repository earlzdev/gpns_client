package com.earl.gpns.ui.search.companion

import android.content.Context
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import com.earl.gpns.R
import com.earl.gpns.domain.Interactor
import com.earl.gpns.ui.search.SpinnerInterfaceInitializator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompanionFormViewModel @Inject constructor(
    private val interactor: Interactor,
): ViewModel(), SpinnerInterfaceInitializator {

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
}