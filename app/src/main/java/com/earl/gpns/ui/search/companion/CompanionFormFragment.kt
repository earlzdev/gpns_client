package com.earl.gpns.ui.search.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.R
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentCompanionFormBinding
import com.earl.gpns.ui.models.CompanionFormUi
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
        binding.finishDriverForm.setOnClickListener {
            sendNewCompanionForm()
        }
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
    }

    private fun initViews() {
        val compDriveFrom = binding.spinnerCompanionFrom
        compDriveFrom.adapter = viewModel.initSpinnerAdapter(R.array.spinner_drive_from, requireContext())
        compDriveFrom.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.selectedItem == "Другой район") {
                    binding.spinnerCompanionFrom.isVisible = false
                    binding.compFromAnotherDistrict.isVisible = true
                    binding.closeAnotherDistrictView.isVisible = true
                }
                binding.closeAnotherDistrictView.setOnClickListener {
                    binding.spinnerCompanionFrom.setSelection(0)
                    binding.spinnerCompanionFrom.isVisible = true
                    binding.compFromAnotherDistrict.isVisible = false
                    binding.closeAnotherDistrictView.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        val compDriveTo = binding.spinnerCompanionDriveTo
        compDriveTo.adapter = viewModel.initSpinnerAdapter(R.array.spinner_drive_to, requireContext())
        compDriveTo.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.selectedItem == "Другое") {
                    binding.spinnerCompanionDriveTo.isVisible = false
                    binding.anotherCompDriveTo.isVisible = true
                    binding.closeAnotherDistrictDriveTo.isVisible = true
                }
                binding.closeAnotherDistrictDriveTo.setOnClickListener {
                    binding.spinnerCompanionDriveTo.setSelection(0)
                    binding.spinnerCompanionDriveTo.isVisible = true
                    binding.anotherCompDriveTo.isVisible = false
                    binding.closeAnotherDistrictDriveTo.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
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
                    binding.spinnerActualTripTime.setSelection(0)
                    binding.spinnerActualTripTime.isVisible = true
                    binding.anotherActualTripTime.isVisible = false
                    binding.closeAnotherActualTripTime.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun validate() : Boolean {
        val validation = CompanionFormValidation.Base(
            binding.spinnerCompanionFrom,
            binding.compFromAnotherDistrict,
            binding.spinnerCompanionDriveTo,
            binding.anotherCompDriveTo,
            binding.spinnerActualTripTime,
            binding.anotherActualTripTime
        )
        return validation.validate()
    }

    private fun sendNewCompanionForm() {
        if (validate()) {
            val from = if (binding.spinnerCompanionFrom.isVisible) {
                binding.spinnerCompanionFrom.selectedItem.toString()
            } else {
                binding.compFromAnotherDistrict.text.toString()
            }
            val to = if (binding.spinnerCompanionDriveTo.isVisible) {
                binding.spinnerCompanionDriveTo.selectedItem.toString()
            } else {
                binding.anotherCompDriveTo.text.toString()
            }
            val actualTripTime = if (binding.spinnerActualTripTime.isVisible) {
                binding.spinnerActualTripTime.selectedItem.toString()
            } else {
                binding.anotherActualTripTime.text.toString()
            }
            val form = CompanionFormUi.Base(
                preferenceManager.getString(Keys.KEY_NAME) ?: "",
                preferenceManager.getString(Keys.KEY_IMAGE) ?: "",
                from,
                to,
                binding.spinnerSchedule.selectedItem.toString(),
                actualTripTime,
                binding.compPriceEd.text.toString() ?: "",
                binding.editTextDriverComment.text.toString() ?: ""
            )
            preferenceManager.putBoolean(Keys.HAS_SEARCH_FORM, true)
            preferenceManager.putBoolean(Keys.IS_DRIVER, false)
            viewModel.sendNewCompanionForm(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                form
            )
        }
    }

    companion object {

        fun newInstance() = CompanionFormFragment()
    }
}