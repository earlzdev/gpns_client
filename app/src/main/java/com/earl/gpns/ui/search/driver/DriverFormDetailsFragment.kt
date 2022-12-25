package com.earl.gpns.ui.search.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentDriverFormDetailsBinding
import com.earl.gpns.ui.SearchFormsDetails
import com.earl.gpns.ui.models.DriverDetails

class DriverFormDetailsFragment(
    private val details: SearchFormsDetails
) : BaseFragment<FragmentDriverFormDetailsBinding>() {

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDriverFormDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
    }

    private fun initViews() {
        details as DriverDetails
        binding.userName.text = details.username
        binding.from.text = details.from
        binding.goesTo.text = details.to
        binding.schedule.text = details.schedule
        binding.ableToCatchCompFrom.text = details.catchCompanionFrom ?: "По договоренности"
        binding.canDriveInTurn.text = if (details.ableToDriveInTurn == 1) "Да" else "Нет"
        binding.alsoAbleToVisit.text = details.alsoCanDriveTo ?: "По договоренности"
        binding.tripTime.text = details.actualTripTime
        binding.avaliablePlaces.text = details.passengersCount.toString()
        binding.tripPrice.text = details.tripPrice.toString() ?: "По договоренности"
        binding.comment.text = details.driverComment
    }

    companion object {

        fun newInstance(details: SearchFormsDetails) = DriverFormDetailsFragment(details)
    }
}