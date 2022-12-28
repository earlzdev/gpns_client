package com.earl.gpns.ui.search.driver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.R
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentDriverFormDetailsBinding
import com.earl.gpns.ui.SearchFormsDetails
import com.earl.gpns.ui.models.DriverDetailsUi
import com.earl.gpns.ui.models.TripNotificationUi
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DriverFormDetailsFragment(
    private val details: SearchFormsDetails,
    private val viewRegime: String
) : BaseFragment<FragmentDriverFormDetailsBinding>() {

    private lateinit var viewModel: DriverFormViewModel

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDriverFormDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DriverFormViewModel::class.java]
        initViews()
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
        binding.seggustDriveTogether.setOnClickListener {
            if (viewRegime != NOTIFICATION) inviteDriver()  // todo send agree
        }
    }

    private fun initViews() {
        details as DriverDetailsUi
        when (viewRegime) {
            NOTIFICATION -> {
                if (details.username == preferenceManager.getString(Keys.KEY_NAME)) {
                    binding.seggustDriveTogether.visibility = View.GONE
                    binding.deny.visibility = View.GONE
                } else {
                    binding.seggustDriveTogether.text = getString(R.string.agree)
                    binding.deny.isVisible = true
                }
            }
            DETAILS -> {

            }
        }
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

    private fun inviteDriver() {
        var existedNotificationsList = mutableListOf<TripNotificationUi>()
        Log.d("tag", "inviteDriver:  existed $existedNotificationsList")
        viewModel.fetchExistedNotificationsFromDb()
        viewModel.observeTripNotificationsLiveData(this) {
            existedNotificationsList = it.toMutableList()
        }
        if (preferenceManager.getBoolean(Keys.HAS_SEARCH_FORM) && !preferenceManager.getBoolean(Keys.IS_DRIVER)) {
            val notification = TripNotificationUi.Base(
                UUID.randomUUID().toString(),
                preferenceManager.getString(Keys.KEY_NAME) ?: "",
                binding.userName.text.toString(),
                COMPANION_ROLE,
                DRIVER_ROLE,
                INVITE,
                ""
            )
            viewModel.inviteDriver(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                notification
            )
            Toast.makeText(requireContext(), "Приглашение отправлено", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Чтобы отправить уведомление этому пользователю, нужно иметь активную анкету попутчика!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        fun newInstance(details: SearchFormsDetails, viewRegime: String) = DriverFormDetailsFragment(details, viewRegime)
        private const val DRIVER_ROLE = "DRIVER_ROLE"
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val INVITE = 1
        private const val DETAILS = "DETAILS"
        private const val NOTIFICATION = "NOTIFICATION"
    }
}