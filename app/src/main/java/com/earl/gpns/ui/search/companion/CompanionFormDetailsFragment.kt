package com.earl.gpns.ui.search.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentCompanionFormDetailsBinding
import com.earl.gpns.ui.SearchFormsDetails
import com.earl.gpns.ui.models.CompanionDetails

class CompanionFormDetailsFragment(
    private val details: SearchFormsDetails
) : BaseFragment<FragmentCompanionFormDetailsBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompanionFormDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        binding.backBtn.setOnClickListener {
            navigator.back()
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

    companion object {

        fun newInstance(details: SearchFormsDetails) = CompanionFormDetailsFragment(details)
    }
}