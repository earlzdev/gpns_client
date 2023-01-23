package com.earl.gpns.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentProfileBinding
import com.earl.gpns.ui.SearchFormsDetails
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private lateinit var viewModel: ProfileViewModel

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        initViews()
    }

    private fun initViews() {
        binding.userName.text = preferenceManager.getString(Keys.KEY_NAME) ?: "Здесь должно было быть ваше имя"
        binding.exitBtn.setOnClickListener {
            logOut()
        }
        binding.myTripForm.setOnClickListener {
            if (preferenceManager.getBoolean(Keys.HAS_SEARCH_FORM)) {
                if (preferenceManager.getBoolean(Keys.IS_DRIVER)) {
                    viewModel.fetchDriverFormFromLocalDb()
                    viewModel.observeDriverTripFormLiveData(this) {
//                        it.provideDriverFormDetailsUi()
                        navigator.driverFormDetails(it.provideDriverFormDetailsUi() as SearchFormsDetails, OWN_TRIP_FORM, "")
                    }
                } else {
                    viewModel.fetchCompanionFormFromDb()
                    viewModel.observeCompanionTripFormLiveData(this) {
//                        it.provideCompanionDetailsUi()
                        navigator.companionFormDetails(it.provideCompanionDetailsUi() as SearchFormsDetails, OWN_TRIP_FORM, "")
                    }
                }
            } else {
                Toast.makeText(requireContext(), "У вас нет формы попутчика", Toast.LENGTH_SHORT).show()
            }
            binding.smth.setOnClickListener {
                if (preferenceManager.getBoolean(Keys.IS_DRIVER)) {
                    viewModel.deleteDriverFormForm(preferenceManager.getString(Keys.KEY_JWT) ?: "")
                } else {
                    viewModel.deleteCompanionForm(preferenceManager.getString(Keys.KEY_JWT) ?: "")
                }
                preferenceManager.putBoolean(Keys.HAS_SEARCH_FORM, false)
                preferenceManager.putBoolean(Keys.IS_DRIVER, false)
                preferenceManager.putBoolean(Keys.IS_STILL_IN_COMP_GROUP, false)
            }
        }
    }

    private fun logOut() {
        navigator.showProgressBar()
        preferenceManager.clear()
        preferenceManager.putBoolean(Keys.KEY_IS_SIGNED_UP, false)
        viewModel.logOut()
        navigator.hideProgressBar()
        navigator.start()
    }

    companion object {

        fun newInstance() = ProfileFragment()
        private const val OWN_TRIP_FORM = "OWN_TRIP_FORM"
    }
}