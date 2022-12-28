package com.earl.gpns.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentProfileBinding
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
        binding.exitBtn.setOnClickListener {
            logOut()
        }
        binding.deleteTripForm.setOnClickListener {
            if (preferenceManager.getBoolean(Keys.IS_DRIVER)) {
                viewModel.deleteDriverFormForm(preferenceManager.getString(Keys.KEY_JWT) ?: "")
            } else {
                viewModel.deleteCompanionForm(preferenceManager.getString(Keys.KEY_JWT) ?: "")
            }
            preferenceManager.putBoolean(Keys.HAS_SEARCH_FORM, false)
            preferenceManager.putBoolean(Keys.IS_DRIVER, false)
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
    }
}