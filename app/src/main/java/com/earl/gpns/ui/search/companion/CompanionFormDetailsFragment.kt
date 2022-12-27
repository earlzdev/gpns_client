package com.earl.gpns.ui.search.companion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentCompanionFormDetailsBinding
import com.earl.gpns.ui.SearchFormsDetails
import com.earl.gpns.ui.models.CompanionDetails
import com.earl.gpns.ui.models.TripNotificationUi
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class CompanionFormDetailsFragment(
    private val details: SearchFormsDetails
) : BaseFragment<FragmentCompanionFormDetailsBinding>() {

    private lateinit var viewModel: CompanionFormViewModel

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompanionFormDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CompanionFormViewModel::class.java]
        initViews()
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
        binding.suggestPlace.setOnClickListener {
            inviteCompanion()
        }
    }

    private fun initViews() {
        details as CompanionDetails
        binding.userName.text = details.username
        binding.from.text = details.from
        binding.driveTo.text = details.to
        binding.schedule.text = details.schedule
        binding.tripTime.text = details.actualTripTime ?: "Не указано"
        binding.price.text = details.ableToPay ?: "По договоренности"
        binding.comment.text = details.comment ?: "Не указано"
    }

    private fun inviteCompanion() {
        if (preferenceManager.getBoolean(Keys.HAS_SEARCH_FORM) && preferenceManager.getBoolean(Keys.IS_DRIVER)) {
            val notification = TripNotificationUi.Base(
                UUID.randomUUID().toString(),
                preferenceManager.getString(Keys.KEY_NAME) ?: "",
                binding.userName.text.toString(),
                DRIVER_ROLE,
                COMPANION_ROLE,
                INVITE,
                ""
            )
            viewModel.inviteCompanion(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                notification
            )
            Toast.makeText(requireContext(), "sended", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Несовпадение условий", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        fun newInstance(details: SearchFormsDetails) = CompanionFormDetailsFragment(details)
        private const val DRIVER_ROLE = "DRIVER_ROLE"
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val INVITE = 1
    }
}