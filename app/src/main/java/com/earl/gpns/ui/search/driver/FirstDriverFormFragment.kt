package com.earl.gpns.ui.search.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.R
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentDriverFormFirstBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstDriverFormFragment : BaseFragment<FragmentDriverFormFirstBinding>() {

    private lateinit var viewModel: NewDriverFormViewModel

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDriverFormFirstBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NewDriverFormViewModel::class.java]
        initSpinners()
        binding.nextBtn.setOnClickListener {
            navigator.startSecondDriverForm()
        }
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
    }

    private fun initSpinners() {
        val spinnerDriveFrom = binding.spinnerDriveFrom
        spinnerDriveFrom.adapter = viewModel.initSpinnerAdapter(R.array.spinner_drive_from, requireContext())
        spinnerDriveFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                   if (p0?.selectedItem == "Другой район") {
                       binding.spinnerDriveFrom.isVisible = false
                       binding.driveFromAnotherDistrict.isVisible = true
                       binding.closeAnotherDistrictView.isVisible = true
                   }
                binding.closeAnotherDistrictView.setOnClickListener {
                    binding.spinnerDriveFrom.isVisible = true
                    binding.driveFromAnotherDistrict.isVisible = false
                    binding.closeAnotherDistrictView.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        val spinnerCanCatchCompFrom = binding.spinnerCanCatchCompFrom
        spinnerCanCatchCompFrom.adapter = viewModel.initSpinnerAdapter(R.array.spinner_can_catch_comp_from, requireContext())
        val spinnerDriveTo = binding.spinnerDriveTo
        spinnerDriveTo.adapter = viewModel.initSpinnerAdapter(R.array.spinner_drive_to, requireContext())
        spinnerDriveTo.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.selectedItem == "Другое") {
                    binding.spinnerDriveTo.isVisible = false
                    binding.driveToAnotherPlace.isVisible = true
                    binding.closeAnotherDriveToAnotherPlace.isVisible = true
                }
                binding.closeAnotherDriveToAnotherPlace.setOnClickListener {
                    binding.spinnerDriveTo.isVisible = true
                    binding.driveToAnotherPlace.isVisible = false
                    binding.closeAnotherDriveToAnotherPlace.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        val spinnerCanAlsoDriveTo = binding.spinnerCanAlsoDriveTo
        spinnerCanAlsoDriveTo.adapter = viewModel.initSpinnerAdapter(R.array.spinner_can_also_drive_to, requireContext())
        val spinnerSchedule = binding.spinnerSchedule
        spinnerSchedule.adapter = viewModel.initSpinnerAdapter(R.array.spinner_schedule, requireContext())
        val spinnerHowLongTripWouldBeActual = binding.spinnerHowLongTripIsActual
        spinnerHowLongTripWouldBeActual.adapter = viewModel.initSpinnerAdapter(R.array.spinner_how_long_trip_is_actual, requireContext())
        spinnerHowLongTripWouldBeActual.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.selectedItem == "Другое") {
                    binding.spinnerHowLongTripIsActual.isVisible = false
                    binding.anotherActualTripTime.isVisible = true
                    binding.closeAnotherActualTripTime.isVisible = true
                }
                binding.closeAnotherActualTripTime.setOnClickListener {
                    binding.spinnerHowLongTripIsActual.isVisible = true
                    binding.anotherActualTripTime.isVisible = false
                    binding.closeAnotherActualTripTime.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun initItemSelectedListeners() {

    }

    companion object {

        fun newInstance() = FirstDriverFormFragment()
    }
}