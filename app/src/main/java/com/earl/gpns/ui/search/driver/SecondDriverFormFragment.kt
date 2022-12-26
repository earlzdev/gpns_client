package com.earl.gpns.ui.search.driver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.R
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentDriverFormSecondBinding
import com.earl.gpns.ui.models.DriverFormUi
import com.earl.gpns.ui.models.FirstPartOfNewDriverForm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class SecondDriverFormFragment(
    private val firstPartOfNewDriverForm: FirstPartOfNewDriverForm
): BaseFragment<FragmentDriverFormSecondBinding>() {

    private lateinit var viewModel: DriverFormViewModel

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDriverFormSecondBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DriverFormViewModel::class.java]
        initViews()
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
        binding.finishDriverForm.setOnClickListener {
            Toast.makeText(requireContext(), "ENDED", Toast.LENGTH_SHORT).show()
            sendNewDriverForm()
        }
    }

    private fun initViews() {
        val spinnerDriverCar = binding.spinnerDriverCarModel
        val spinnerDriverCarModel = binding.spinnerDriverCarType
        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.audi_models, requireContext())
        spinnerDriverCar.adapter = viewModel.initSpinnerAdapter(R.array.spinner_driver_car_model, requireContext())
        spinnerDriverCar.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p0?.selectedItem) {
                    requireContext().resources.getString(R.string.audi) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.audi_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.bmw) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.bmw_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.chevrolet) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.chevrolet_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.daewoo) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.daweoo_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.datsun) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.datsun_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.ford) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.ford_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.honda) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.honda_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.hyundai) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.hyundai_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.kia) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.kia_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.lada) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.avtovaz_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.mazda) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.mazda_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.mitsubishi) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.mitsubishi, requireContext())
                    }
                    requireContext().resources.getString(R.string.mercedes) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.mersedes_models, requireContext())
                    }
                    requireContext().resources.getString(R.string.nissan) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.nissan, requireContext())
                    }
                    requireContext().resources.getString(R.string.opel) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.opel, requireContext())
                    }
                    requireContext().resources.getString(R.string.renault) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.renault, requireContext())
                    }
                    requireContext().resources.getString(R.string.skoda) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.skoda, requireContext())
                    }
                    requireContext().resources.getString(R.string.tesla) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.tesla, requireContext())
                    }
                    requireContext().resources.getString(R.string.toyota) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.toyota, requireContext())
                    }
                    requireContext().resources.getString(R.string.volkswagen) -> {
                        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.volkswagen, requireContext())
                    }
                    requireContext().resources.getString(R.string.another_car) -> {
                        binding.spinnerDriverCarModel.isVisible = false
                        binding.spinnerDriverCarType.isVisible = false
                        binding.anotherAutoModel.isVisible = true
                        binding.anotherAuto.isVisible = true
                        binding.closeAnotherAuto.isVisible = true
                        binding.closeAnotherAuto.setOnClickListener {
                            binding.spinnerDriverCarModel.setSelection(0)
                            binding.spinnerDriverCarModel.isVisible = true
                            binding.anotherAuto.isVisible = false
                            binding.closeAnotherAuto.isVisible = false
                            binding.spinnerDriverCarType.isVisible = true
                            binding.anotherAutoModel.isVisible = false
                        }
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        spinnerDriverCarModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.selectedItem == requireContext().resources.getString(R.string.another_car)) {
                    showInputFieldForAnotherValue()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        val spinnerDriverCarColor = binding.spinnerDriverCarColor
        spinnerDriverCarColor.adapter = viewModel.initSpinnerAdapter(R.array.spinner_driver_car_color, requireContext())
        spinnerDriverCarColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.selectedItem == requireContext().resources.getString(R.string.another_color)) {
                    binding.spinnerDriverCarColor.isVisible = false
                    binding.anotherCarColor.isVisible = true
                    binding.closeAnotherCarColor.isVisible = true
                    binding.closeAnotherCarColor.setOnClickListener {
                        binding.spinnerDriverCarColor.setSelection(0)
                        binding.spinnerDriverCarColor.isVisible = true
                        binding.anotherCarColor.isVisible = false
                        binding.closeAnotherCarColor.isVisible = false
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        val spinnerHowManyPlacesInCar = binding.spinnerDriverCarCountOfPassengers
        spinnerHowManyPlacesInCar.adapter = viewModel.initSpinnerAdapter(R.array.spinner_how_many_places_in_driver_car, requireContext())
    }

    private fun showInputFieldForAnotherValue() {
        binding.spinnerDriverCarType.isVisible = false
        binding.anotherAutoModel.isVisible = true
        binding.closeAnotherAutoModel.isVisible = true
        binding.closeAnotherAutoModel.setOnClickListener {
            binding.spinnerDriverCarType.setSelection(0)
            binding.spinnerDriverCarType.isVisible = true
            binding.anotherAutoModel.isVisible = false
            binding.closeAnotherAutoModel.isVisible = false
        }
    }

    private fun validate() : Boolean {
        val validation = SecondDriverFormValidation.Base(
            binding.spinnerDriverCarModel,
            binding.anotherAuto,
            binding.spinnerDriverCarColor,
            binding.anotherCarColor
        )
        return validation.validate()
    }

    private fun sendNewDriverForm() {
        if (validate()) {
            val car = if (binding.spinnerDriverCarModel.isVisible) {
                binding.spinnerDriverCarModel.selectedItem.toString()
            } else {
                binding.anotherAuto.text.toString()
            }
            val carModel = if (!binding.spinnerDriverCarModel.isVisible && !binding.spinnerDriverCarType.isVisible) {
                binding.anotherAutoModel.text.toString()
            } else if (binding.spinnerDriverCarType.isVisible) {
                binding.spinnerDriverCarType.selectedItem.toString()
            } else {
                binding.anotherAutoModel.text.toString()
            }
            val carColor = if (binding.spinnerDriverCarColor.isVisible) {
                binding.spinnerDriverCarColor.selectedItem.toString()
            } else {
                binding.anotherCarColor.text.toString()
            }
            val driverForm = DriverFormUi.Base(
                preferenceManager.getString(Keys.KEY_NAME) ?: "",
                preferenceManager.getString(Keys.KEY_IMAGE) ?: "",
                firstPartOfNewDriverForm.driveFrom,
                firstPartOfNewDriverForm.driveTo,
                firstPartOfNewDriverForm.catchCompanionFrom ?: "",
                firstPartOfNewDriverForm.alsoCanDriveTo ?: "",
                firstPartOfNewDriverForm.schedule,
                firstPartOfNewDriverForm.ableToDriveInTurn,
                firstPartOfNewDriverForm.actualTripTime,
                car,
                carModel ?: "",
                carColor,
                binding.spinnerDriverCarCountOfPassengers.selectedItem.toString(),
                binding.editTextGosNumberOfCar.text.toString() ?: "",
                binding.editTextTripPrice.text.toString() ?: "",
                binding.editTextDriverComment.text.toString() ?: ""
            )
            Log.d("tag", "sendNewDriverForm: DRIVER FORM -> ${Json.encodeToString(driverForm)}")
            preferenceManager.putBoolean(Keys.HAS_SEARCH_FORM, true)
            preferenceManager.putBoolean(Keys.IS_DRIVER, true)
            viewModel.sendNewDriverForm(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                driverForm
            )
        }
    }

    companion object {

        fun newInstance(firstPartOfNewDriverForm: FirstPartOfNewDriverForm) = SecondDriverFormFragment(firstPartOfNewDriverForm)
    }
}