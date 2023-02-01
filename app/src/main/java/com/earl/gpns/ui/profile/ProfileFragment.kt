package com.earl.gpns.ui.profile

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.R
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.Keys
import com.earl.gpns.databinding.FragmentProfileBinding
import com.earl.gpns.ui.about.AboutAppFragment
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
        binding.userName.text = preferenceManager.getString(Keys.KEY_NAME)
        binding.exitBtn.setOnClickListener {
            logOut()
        }
        binding.myTripForm.setOnClickListener {
            if (preferenceManager.getBoolean(Keys.HAS_SEARCH_FORM)) {
                if (preferenceManager.getBoolean(Keys.IS_DRIVER)) {
                    viewModel.fetchDriverFormFromLocalDb()
                    viewModel.observeDriverTripFormLiveData(this) {
                        navigator.driverFormDetails(it.provideDriverFormDetailsUi() as SearchFormsDetails, OWN_TRIP_FORM, "")
                    }
                } else {
                    viewModel.fetchCompanionFormFromDb()
                    viewModel.observeCompanionTripFormLiveData(this) {
                        navigator.companionFormDetails(it.provideCompanionDetailsUi() as SearchFormsDetails, OWN_TRIP_FORM, "")
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.u_dont_have_comp_form), Toast.LENGTH_SHORT).show()
            }
        }
        binding.communicateWithDeveloper.setOnClickListener {
            if (preferenceManager.getBoolean(Keys.IS_DRIVER)) {
                viewModel.deleteDriverFormForm(preferenceManager.getString(Keys.KEY_JWT) ?: "")
            } else {
                viewModel.deleteCompanionForm(preferenceManager.getString(Keys.KEY_JWT) ?: "")
            }
            preferenceManager.putBoolean(Keys.HAS_SEARCH_FORM, false)
            preferenceManager.putBoolean(Keys.IS_DRIVER, false)
            preferenceManager.putBoolean(Keys.IS_STILL_IN_COMP_GROUP, false)
        }
        binding.safePolitics.setOnClickListener { navigator.privacyPolicyFragment() }
        binding.aboutApp.setOnClickListener {
            AboutAppFragment.newInstance().show(parentFragmentManager.beginTransaction(), AboutAppFragment.TAG)
        }
        binding.shareBtn.setOnClickListener {
            share()
        }
        binding.rate.setOnClickListener {
            rate()
        }
        binding.communicateWithDeveloper.setOnClickListener {
            communicateWithDev()
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

    private fun share() {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.search_comps_app))
        email.putExtra(
            Intent.EXTRA_TEXT, getString(R.string.recommend) +
                "\n ${"https://play.google.com/store/apps/details?id=${requireActivity().packageName}"}")
        email.type = getString(R.string.msg_interface)
        startActivity(Intent.createChooser(email, getString(R.string.choose_email_clinet)))
    }

    private fun rate() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${requireActivity().packageName}")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${requireActivity().packageName}")))
        }
    }

    private fun communicateWithDev() {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts(getString(R.string.mailto), getString(R.string.my_email), null)
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.search_comps_appp))
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.choose_messenger)))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                requireActivity(),
                R.string.no_emails_client,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {

        fun newInstance() = ProfileFragment()
        private const val OWN_TRIP_FORM = "OWN_TRIP_FORM"
    }
}