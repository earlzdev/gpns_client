package com.earl.gpns.ui.search.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.R
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentCompanionFormBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompanionFormFragment : BaseFragment<FragmentCompanionFormBinding>() {

    private lateinit var viewModel: CompanionFormViewModel

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompanionFormBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CompanionFormViewModel::class.java]
        initViews()
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
    }

    private fun initViews() {
        val compDriveFrom = binding.spinnerCompanionFrom
        compDriveFrom.adapter = viewModel.initSpinnerAdapter(R.array.spinner_drive_from, requireContext())
        val compDriveTo = binding.spinnerCompanionDriveTo
        compDriveTo.adapter = viewModel.initSpinnerAdapter(R.array.spinner_drive_to, requireContext())
        val compSchedule = binding.spinnerSchedule
        compSchedule.adapter = viewModel.initSpinnerAdapter(R.array.spinner_schedule, requireContext())
        val compTripTimeAbility = binding.spinnerActualTripTime
        compTripTimeAbility.adapter = viewModel.initSpinnerAdapter(R.array.spinner_how_long_trip_is_actual, requireContext())
        compTripTimeAbility.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.selectedItem == "Другое") {
                    binding.spinnerActualTripTime.isVisible = false
                    binding.anotherActualTripTime.isVisible = true
                    binding.closeAnotherActualTripTime.isVisible = true
                }
                binding.closeAnotherActualTripTime.setOnClickListener {
                    binding.spinnerActualTripTime.isVisible = true
                    binding.anotherActualTripTime.isVisible = false
                    binding.closeAnotherActualTripTime.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    companion object {

        fun newInstance() = CompanionFormFragment()
    }
}