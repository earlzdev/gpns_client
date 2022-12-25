package com.earl.gpns.ui.search.driver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.earl.gpns.R
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentDriverFormFirstBinding
import com.earl.gpns.ui.models.FirstPartOfNewDriverForm
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
            goToSecondPartOfDriverForm()
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
                    binding.spinnerDriveFrom.setSelection(0)
                    binding.spinnerDriveFrom.isVisible = true
                    binding.driveFromAnotherDistrict.isVisible = false
                    binding.closeAnotherDistrictView.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
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
                    binding.spinnerDriveTo.setSelection(0)
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
                    binding.spinnerHowLongTripIsActual.setSelection(0)
                    binding.spinnerHowLongTripIsActual.isVisible = true
                    binding.anotherActualTripTime.isVisible = false
                    binding.closeAnotherActualTripTime.isVisible = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val multiSpinnerCanCatchCompFrom = binding.multispinnerCanCatchCompFrom
        multiSpinnerCanCatchCompFrom.isSearchEnabled = false
        multiSpinnerCanCatchCompFrom.setClearText("Стереть и закрыть")
        val catchFromSpinnerData = requireContext().resources.getStringArray(R.array.spinner_can_catch_comp_from).toList()
        val catchFromSpinnerDataList = mutableListOf<KeyPairBoolData>()
        val catchFromSpinnerReadyList = mutableListOf<String>()
        for (i in catchFromSpinnerData.indices) {
            val newPair = KeyPairBoolData()
            newPair.id = i.toLong()
            newPair.name = catchFromSpinnerData[i]
            newPair.isSelected = false
            catchFromSpinnerDataList.add(newPair)
        }
        multiSpinnerCanCatchCompFrom.setItems(catchFromSpinnerDataList) {
            for (i in catchFromSpinnerDataList.indices) {
                if (catchFromSpinnerDataList[i].isSelected) {
                    Log.d("tag", "initSpinners: ${catchFromSpinnerDataList[i].name}")
                    catchFromSpinnerReadyList.add(catchFromSpinnerDataList[i].name)
                }
            }
        }
        multiSpinnerCanCatchCompFrom.setLimit(5) {
            Toast.makeText(requireContext(), "Можно выбрать не более 5 мест", Toast.LENGTH_SHORT).show()
        }
        val multiSelectSpinnerCanAlsoDriveTo = binding.spinnerCanAlsoDriveTo
        multiSelectSpinnerCanAlsoDriveTo.isSearchEnabled = false
        multiSelectSpinnerCanAlsoDriveTo.setClearText("Стереть и закрыть")
        val canAlsoDriveToSpinnerData = requireContext().resources.getStringArray(R.array.spinner_can_also_drive_to).toList()
        val canAlsoDriveToSpinnerDataList = mutableListOf<KeyPairBoolData>()
        val canAlsoDriveToSpinnerReadyList = mutableListOf<String>()
        for (i in canAlsoDriveToSpinnerData.indices) {
            val newPair = KeyPairBoolData()
            newPair.id = i.toLong()
            newPair.name = canAlsoDriveToSpinnerData[i]
            newPair.isSelected = false
            canAlsoDriveToSpinnerDataList.add(newPair)
        }
        multiSelectSpinnerCanAlsoDriveTo.setItems(canAlsoDriveToSpinnerDataList) {
            for (i in canAlsoDriveToSpinnerDataList.indices) {
                if (canAlsoDriveToSpinnerDataList[i].isSelected) {
                    Log.d("tag", "initSpinners: ${canAlsoDriveToSpinnerDataList[i].name}")
                    canAlsoDriveToSpinnerReadyList.add(canAlsoDriveToSpinnerDataList[i].name)
                }
            }
        }
        multiSelectSpinnerCanAlsoDriveTo.setLimit(5) {
            Toast.makeText(requireContext(), "Можно выбрать не более 5 мест", Toast.LENGTH_SHORT).show()
        }

    }

    private fun validate() : Boolean {
        val validation = FirstDriverFormValidation.Base(
            binding.spinnerDriveFrom,
            binding.driveFromAnotherDistrict,
            binding.spinnerDriveTo,
            binding.driveToAnotherPlace,
            binding.spinnerHowLongTripIsActual,
            binding.anotherActualTripTime
        )
        return validation.validate()
    }

    private fun goToSecondPartOfDriverForm() {
        if (validate()) {
            val from = if (binding.spinnerDriveFrom.isVisible) {
                binding.spinnerDriveFrom.selectedItem.toString()
            } else {
                binding.driveFromAnotherDistrict.text.toString()
            }
            val actualTripTime = if (binding.spinnerHowLongTripIsActual.isVisible) {
                binding.spinnerHowLongTripIsActual.selectedItem.toString()
            } else {
                binding.anotherActualTripTime.text.toString()
            }
            val firstPartOfDriverForm = FirstPartOfNewDriverForm(
                from,
                binding.spinnerDriveTo.selectedItem.toString(),
                binding.multispinnerCanCatchCompFrom.selectedItems.map { it.name }.toList().toString(),
                binding.spinnerCanAlsoDriveTo.selectedItems.map { it.name }.toList().toString(),
                binding.spinnerSchedule.selectedItem.toString(),
                if (binding.chechboxDriveInTurn.isChecked) 1 else 0,
                actualTripTime
            )
            navigator.startSecondDriverForm(firstPartOfDriverForm)
        }
    }

    companion object {

        fun newInstance() = FirstDriverFormFragment()
    }
}