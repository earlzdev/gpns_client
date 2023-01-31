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
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.Keys
import com.earl.gpns.databinding.FragmentDriverFormDetailsBinding
import com.earl.gpns.ui.SearchFormsDetails
import com.earl.gpns.ui.models.DriverDetailsUi
import com.earl.gpns.ui.models.TripNotificationUi
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DriverFormDetailsFragment(
    private val details: SearchFormsDetails,
    private val viewRegime: String,
    private val notificationId: String
) : BaseFragment<FragmentDriverFormDetailsBinding>() {

    private lateinit var viewModel: DriverFormViewModel
    private var notificationSent: Boolean = false
    private var isInvitationAnswered = false

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
        viewModel.fetchUsersListInCompanionGroup()
    }

    private fun initClickListeners() {
        binding.backBtn.setOnClickListener {
//            navigator.back()
            when(viewRegime) {
                NOTIFICATION -> navigator.popBackStackToFragment(tripNotifications)
                DETAILS -> navigator.popBackStackToFragment(mainFragment)
                OWN_INVITE -> navigator.popBackStackToFragment(tripNotifications)
                OWN_TRIP_FORM -> navigator.back()
            }
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
        binding.deleteForm.setOnClickListener {
            preferenceManager.putBoolean(Keys.HAS_SEARCH_FORM, false)
            preferenceManager.putBoolean(Keys.IS_DRIVER, false)
            preferenceManager.putBoolean(Keys.IS_STILL_IN_COMP_GROUP, false)
            viewModel.clearTripFormInLocalDb()
            viewModel.deleteDriverFormForm(preferenceManager.getString(Keys.KEY_JWT) ?: "")
            navigator.back()
            Toast.makeText(requireContext(), "Ваша форма удалена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun acceptDriverToRideTogether(driverUsername: String) {
        if (isInvitationAnswered) {
            Toast.makeText(requireContext(), "Вы уже ответили этому пользователю на приглашение", Toast.LENGTH_SHORT).show()
        } else {
            val existedList = viewModel.provideExistedNotificationsListLiveData()
            if (existedList?.find { it.provideId() == notificationId && it.isActive()} != null) {
                if (!preferenceManager.getBoolean(Keys.IS_STILL_IN_COMP_GROUP)) {
                    viewModel.acceptDriverToRideTogether(
                        preferenceManager.getString(Keys.KEY_JWT) ?: "",
                        driverUsername
                    )
                    viewModel.markTripNotificationAsNotActive(
                        preferenceManager.getString(Keys.KEY_JWT) ?: "",
                        notificationId
                    )
                    preferenceManager.putBoolean(Keys.IS_STILL_IN_COMP_GROUP, true)
                    viewModel.insertNewUserIntoLocalDbCompGroup(driverUsername)
                    isInvitationAnswered = true
                } else {
                    Toast.makeText(requireContext(), "Вы уже договорились здить с другим человеком!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Вы уже отвечали на это приглашение, либо оно уже неактивно", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun denyDriverToDriveTogether(driverUsername: String) {
        if (isInvitationAnswered) {
            Toast.makeText(requireContext(), "Вы уже ответили этому пользователю на приглашение", Toast.LENGTH_SHORT).show()
        } else {
            val existedList = viewModel.provideExistedNotificationsListLiveData()
            if (existedList?.find { it.provideId() == notificationId && it.isActive()} != null) {
                viewModel.markTripNotificationAsNotActive(
                    preferenceManager.getString(Keys.KEY_JWT) ?: "",
                    notificationId
                )
                viewModel.denyDriverToRideTogether(
                    preferenceManager.getString(Keys.KEY_JWT) ?: "",
                    driverUsername
                )
                isInvitationAnswered = true
            } else {
                Toast.makeText(requireContext(), "Вы уже отвечали на это приглашение, либо оно уже неактивно", Toast.LENGTH_SHORT).show()
            }
        }
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
            OWN_TRIP_FORM -> {
                binding.seggustDriveTogether.visibility = View.GONE
                binding.deny.visibility = View.GONE
                binding.deleteForm.visibility = View.VISIBLE
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
        val existedList = viewModel.provideExistedNotificationsListLiveData()?.map { it.provideTripNotificationUiRecyclerItem() }
//            val existedNotification = existedList?.find {  it.receiverName == binding.userName.text.toString() || it.authorName == binding.userName.text.toString()}
        val usersListInCompanionGroup = viewModel.provideCompanionUsersListLiveDataValue() ?: emptyList()
        val existedNotification = existedList?.find {
            (it.receiverName == binding.userName.text.toString()
                    || it.authorName == binding.userName.text.toString())
                    && it.type == INVITE
                    && it.active == ACTIVE
        }
        Log.d("tag", "inviteDriver: existedlist -> $existedList")
        Log.d("tag", "inviteDriver: existed -> $existedNotification")
        if (!preferenceManager.getBoolean(Keys.IS_STILL_IN_COMP_GROUP)) {
            if (!notificationSent) {
                if (!usersListInCompanionGroup.contains(binding.userName.text.toString())) {
                    if (existedNotification == null || existedNotification.active != ACTIVE) {
                        if (preferenceManager.getBoolean(Keys.HAS_SEARCH_FORM) && !preferenceManager.getBoolean(
                                Keys.IS_DRIVER)) {
                            val notificationId = UUID.randomUUID().toString()
                            val notification = TripNotificationUi.Base(
                                notificationId,
                                preferenceManager.getString(Keys.KEY_NAME) ?: "",
                                binding.userName.text.toString(),
                                COMPANION_ROLE,
                                DRIVER_ROLE,
                                INVITE,
                                "",
                                ACTIVE
                            )
                            viewModel.inviteDriver(
                                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                                notification
                            )
//                        viewModel.markTripNotificationAsNotActive(
//                            preferenceManager.getString(Keys.KEY_JWT) ?: "",
//                            notificationId
//                        )
                            Toast.makeText(requireContext(), "Приглашение отправлено", Toast.LENGTH_SHORT).show()
                            notificationSent = true
                        } else {
                            Toast.makeText(requireContext(), "Чтобы отправить уведомление этому пользователю, нужно иметь активную анкету попутчика!", Toast.LENGTH_SHORT).show()
                        }
                    } else if (existedNotification.receiverName == binding.userName.text.toString() && existedNotification.active == ACTIVE) {
                        Toast.makeText(requireContext(), "Вы уже отправяли приглашение этому пользователю, дождитесь ответа", Toast.LENGTH_SHORT).show()
                    } else if (existedNotification.active == ACTIVE) {
                        Toast.makeText(requireContext(), "У вас уже есть приглашение от этого пользователя", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Этот пользователь и так уже ездит с Вами",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "Вы уже отправляли приглашение этому пользователю", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Вы уже состоите в группе попутчиков, отправлять приглашения может только ее создатель", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        fun newInstance(details: SearchFormsDetails, viewRegime: String, notificationId: String) = DriverFormDetailsFragment(details, viewRegime, notificationId)
        private const val DRIVER_ROLE = "DRIVER_ROLE"
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val INVITE = "INVITE"
        private const val DETAILS = "DETAILS"
        private const val NOTIFICATION = "NOTIFICATION"
        private const val OWN_INVITE = "OWN_INVITE"
        private const val ACTIVE = 1
        private const val tripNotifications = "tripNotifications"
        private const val mainFragment = "mainFragment"
        private const val OWN_TRIP_FORM = "OWN_TRIP_FORM"
    }
}