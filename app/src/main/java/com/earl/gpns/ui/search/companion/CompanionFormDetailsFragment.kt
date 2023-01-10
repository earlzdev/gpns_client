package com.earl.gpns.ui.search.companion

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
import com.earl.gpns.databinding.FragmentCompanionFormDetailsBinding
import com.earl.gpns.ui.SearchFormsDetails
import com.earl.gpns.ui.models.CompanionDetailsUi
import com.earl.gpns.ui.models.TripNotificationUi
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CompanionFormDetailsFragment(
    private val details: SearchFormsDetails,
    private val viewRegime: String,
    private val notificationId: String
) : BaseFragment<FragmentCompanionFormDetailsBinding>() {

    private lateinit var viewModel: CompanionFormViewModel
    private var notificationSent: Boolean = false

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompanionFormDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CompanionFormViewModel::class.java]
        initViews()
        initClickListeners()
        viewModel.fetchExistedNotificationsFromDb()
    }

    private fun initClickListeners() {
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
        binding.suggestPlace.setOnClickListener {
            when (viewRegime) {
                NOTIFICATION -> {
                    acceptCompanionToRideTogether(binding.userName.text.toString())
                }
                DETAILS -> {
                    inviteCompanion()
                }
            }
        }
        binding.deny.setOnClickListener {
            denyCompanionToDriveTogether(binding.userName.text.toString())
        }
    }

    private fun acceptCompanionToRideTogether(companionUsername: String) {
        if (!preferenceManager.getBoolean(Keys.IS_STILL_IN_COMP_GROUP) || preferenceManager.getBoolean(Keys.IS_DRIVER)) {
            viewModel.acceptCompanionToRideTogether(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                companionUsername
            )
            viewModel.markTripNotificationAsNotActive(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                notificationId
            )
            preferenceManager.putBoolean(Keys.IS_STILL_IN_COMP_GROUP, true)
        } else {
            Toast.makeText(requireContext(), "Вы уже договорились ездить с другим человеком!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun denyCompanionToDriveTogether(companionUsername: String) {
        viewModel.denyCompanionToRideTogether(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            companionUsername
        )
    }

    private fun initViews() {
        details as CompanionDetailsUi
        when (viewRegime) {
            NOTIFICATION -> {
                binding.suggestPlace.text = getString(R.string.agree)
                binding.deny.isVisible = true
            }
            OWN_INVITE -> {
                binding.suggestPlace.visibility = View.GONE
                binding.deny.visibility = View.GONE
            }
            DETAILS -> {

            }
        }
        binding.userName.text = details.username
        binding.from.text = details.from
        binding.driveTo.text = details.to
        binding.schedule.text = details.schedule
        binding.tripTime.text = details.actualTripTime ?: "Не указано"
        binding.price.text = details.ableToPay ?: "По договоренности"
        binding.comment.text = details.comment ?: "Не указано"
    }

    private fun inviteCompanion() {
        viewModel.fetchExistedNotificationsFromDb()
        val existedList = viewModel.provideTripNotificationsLiveData()?.map { it.provideTripNotificationUiRecyclerItem() }
//            val existedNotification = existedList?.find {  it.receiverName == binding.userName.text.toString() || it.authorName == binding.userName.text.toString()}
        val existedNotificationFromDb = existedList?.find {
            (it.receiverName == binding.userName.text.toString()
                    || it.authorName == binding.userName.text.toString())
                    && it.type == INVITE
                    && it.active == ACTIVE
        }
        Log.d("tag", "inviteDriver: existedlist -> $existedList")
        Log.d("tag", "inviteDriver: existed -> $existedNotificationFromDb")

        if (!notificationSent) {
            if (existedNotificationFromDb == null || existedNotificationFromDb.active != ACTIVE) {
                if (preferenceManager.getBoolean(Keys.HAS_SEARCH_FORM) && preferenceManager.getBoolean(Keys.IS_DRIVER)) {
                    val notificationId = UUID.randomUUID().toString()
                    val notification = TripNotificationUi.Base(
                        notificationId,
                        preferenceManager.getString(Keys.KEY_NAME) ?: "",
                        binding.userName.text.toString(),
                        DRIVER_ROLE,
                        COMPANION_ROLE,
                        INVITE,
                        "",
                        ACTIVE
                    )
                    viewModel.inviteCompanion(
                        preferenceManager.getString(Keys.KEY_JWT) ?: "",
                        notification
                    )
//                    viewModel.markTripNotificationAsNotActive(
//                        preferenceManager.getString(Keys.KEY_JWT) ?: "",
//                        notificationId
//                    )
                    Toast.makeText(requireContext(), "Приглашение отправлено", Toast.LENGTH_LONG).show()
                    notificationSent = true
                } else {
                    Toast.makeText(requireContext(), "Чтобы отправить уведомление этому пользователю, нужно иметь активную анкету водителя!", Toast.LENGTH_LONG).show()
                }
            } else if (existedNotificationFromDb.receiverName == binding.userName.text.toString() && existedNotificationFromDb.active == ACTIVE) {
                Toast.makeText(requireContext(), "Вы уже отправяли приглашение этому пользователю, дождитесь ответа", Toast.LENGTH_SHORT).show()
            } else if (existedNotificationFromDb.active == ACTIVE) {
                Toast.makeText(requireContext(), "У вас уже есть приглашение от этого пользователя", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Вы уже отправяли приглашение этому пользователю, дождитесь ответа", Toast.LENGTH_SHORT).show()
        }
    }/* else {
            Toast.makeText(requireContext(), "Вы уже состоите в группе попутчиков, отправлять приглашения может только ее создатель", Toast.LENGTH_SHORT).show()
        }*/
//    }

    companion object {

        fun newInstance(details: SearchFormsDetails, viewRegime: String, notificationId: String) = CompanionFormDetailsFragment(details, viewRegime, notificationId)
        private const val DRIVER_ROLE = "DRIVER_ROLE"
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val INVITE = "INVITE"
        private const val DETAILS = "DETAILS"
        private const val NOTIFICATION = "NOTIFICATION"
        private const val OWN_INVITE = "OWN_INVITE"
        private const val ACTIVE = 1
    }
}