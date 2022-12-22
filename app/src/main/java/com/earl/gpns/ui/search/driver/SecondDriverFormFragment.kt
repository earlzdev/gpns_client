package com.earl.gpns.ui.search.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.R
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.databinding.FragmentDriverFormSecondBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondDriverFormFragment : BaseFragment<FragmentDriverFormSecondBinding>() {

    private lateinit var viewModel: NewDriverFormViewModel

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDriverFormSecondBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NewDriverFormViewModel::class.java]
        initViews()
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
    }

    private fun initViews() {
        val spinnerDriverCarModel = binding.spinnerDriverCarModel
        spinnerDriverCarModel.adapter = viewModel.initSpinnerAdapter(R.array.spinner_driver_car_model, requireContext())
        val spinnerDriverCatType = binding.spinnerDriverCarType
        spinnerDriverCatType.adapter = viewModel.initSpinnerAdapter(R.array.spinner_car_type, requireContext())
        val spinnerDriverCarColor = binding.spinnerDriverCarColor
        spinnerDriverCarColor.adapter = viewModel.initSpinnerAdapter(R.array.spinner_driver_car_color, requireContext())
        val spinnerHowManyPlacesInCar = binding.spinnerDriverCarCountOfPassengers
        spinnerHowManyPlacesInCar.adapter = viewModel.initSpinnerAdapter(R.array.spinner_how_many_places_in_driver_car, requireContext())
    }

    companion object {

        fun newInstance() = SecondDriverFormFragment()
    }
}