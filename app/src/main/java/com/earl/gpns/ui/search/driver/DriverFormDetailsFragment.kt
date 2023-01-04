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
    private var notificationSent: Boolean = false

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDriverFormDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DriverFormViewModel::class.java]
        initViews()
        initClickListeners()
        viewModel.fetchExistedNotificationsFromDb()
    }

    private fun initClickListeners() {
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
        binding.seggustDriveTogether.setOnClickListener {
            when(viewRegime) {
                NOTIFICATION -> {
                    acceptDriverToRideTogether(binding.userName.text.toString())
                }
                DETAILS -> {
                    inviteDriver()
                }
            }
        }
        binding.deny.setOnClickListener {
            denyDriverToDriveTogether(binding.userName.text.toString())
        }
    }

    private fun acceptDriverToRideTogether(driverUsername: String) {
        if (!preferenceManager.getBoolean(Keys.IS_STILL_IN_COMP_GROUP)) {
            viewModel.acceptDriverToRideTogether(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                driverUsername
            )
            preferenceManager.putBoolean(Keys.IS_STILL_IN_COMP_GROUP, true)
        } else {
            Toast.makeText(requireContext(), "Вы уже договорились здить с другим человеком!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun denyDriverToDriveTogether(driverUsername: String) {
        viewModel.denyDriverToRideTogether(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            driverUsername
        )
    }

    private fun initViews() {
        details as DriverDetailsUi
        when (viewRegime) {
            NOTIFICATION -> {
                binding.seggustDriveTogether.text = getString(R.string.agree)
                binding.deny.isVisible = true
            }
            OWN_INVITE -> {
                binding.seggustDriveTogether.visibility = View.GONE
                binding.deny.visibility = View.GONE
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
//        if (preferenceManager.getBoolean(Keys.IS_STILL_IN_COMP_GROUP) && !preferenceManager.getBoolean(Keys.IS_DRIVER)) {
            val existedList = viewModel.provideExistedNotificationsListLiveData()?.map { it.provideTripNotificationUiRecyclerItem() }
            val existedNotification = existedList?.find {  it.receiverName == binding.userName.text.toString() || it.authorName == binding.userName.text.toString()}
            Log.d("tag", "inviteDriver: existedlist -> $existedList")
            Log.d("tag", "inviteDriver: existed -> $existedNotification")
            if (!notificationSent) {
                if (existedNotification == null) {
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
                        notificationSent = true
                    } else {
                        Toast.makeText(requireContext(), "Чтобы отправить уведомление этому пользователю, нужно иметь активную анкету попутчика!", Toast.LENGTH_SHORT).show()
                    }
                } else if (existedNotification.receiverName == binding.userName.text.toString()){
                    Toast.makeText(requireContext(), "Вы уже отправяли приглашение этому пользователю, дождитесь ответа", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "У вас уже есть приглашение от этого пользователя", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Вы уже отправяли приглашение этому пользователю, дождитесь ответа", Toast.LENGTH_SHORT).show()
            }
//        } else {
//            Toast.makeText(requireContext(), "Вы уже договорились ездить вместе с другим человеком!", Toast.LENGTH_SHORT).show()
//        }
    }

    companion object {

        fun newInstance(details: SearchFormsDetails, viewRegime: String) = DriverFormDetailsFragment(details, viewRegime)
        private const val DRIVER_ROLE = "DRIVER_ROLE"
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val INVITE = "INVITE"
        private const val DETAILS = "DETAILS"
        private const val NOTIFICATION = "NOTIFICATION"
        private const val OWN_INVITE = "OWN_INVITE"
    }
}