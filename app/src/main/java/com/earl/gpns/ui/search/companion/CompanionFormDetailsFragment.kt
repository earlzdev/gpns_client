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
    private val viewRegime: String
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
        viewModel.fetchExistedNotificationsFromDb()
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
        binding.suggestPlace.setOnClickListener {
            inviteCompanion()
        }
    }

    private fun initViews() {
        details as CompanionDetailsUi
        when (viewRegime) {
            NOTIFICATION -> {
                if (details.username != preferenceManager.getString(Keys.KEY_NAME)) {
                    binding.suggestPlace.visibility = View.GONE
                    binding.deny.visibility = View.GONE
                } else {
                    binding.suggestPlace.text = getString(R.string.agree)
                    binding.deny.isVisible = true
                }
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
        val existedNotification = existedList?.find {  it.receiverName == binding.userName.text.toString() || it.authorName == binding.userName.text.toString()}
        Log.d("tag", "inviteDriver: existedlist -> $existedList")
        Log.d("tag", "inviteDriver: existed -> $existedNotification")
        if (!notificationSent) {
            if (existedNotification == null) {
                if (preferenceManager.getBoolean(Keys.HAS_SEARCH_FORM) && preferenceManager.getBoolean(Keys.IS_DRIVER)) {
                    val notification = TripNotificationUi.Base(
                        UUID.randomUUID().toString(),
                        preferenceManager.getString(Keys.KEY_NAME) ?: "",
                        binding.userName.text.toString(),
                        DRIVER_ROLE,
                        COMPANION_ROLE,
                        INVITE,
                        ""
                    )
                    viewModel.inviteCompanion(
                        preferenceManager.getString(Keys.KEY_JWT) ?: "",
                        notification
                    )
                    Toast.makeText(requireContext(), "Приглашение отправлено", Toast.LENGTH_LONG).show()
                    notificationSent = true
                } else {
                    Toast.makeText(requireContext(), "Чтобы отправить уведомление этому пользователю, нужно иметь активную анкету водителя!", Toast.LENGTH_LONG).show()
                }
            } else if (existedNotification.receiverName == binding.userName.text.toString()) {
                Toast.makeText(requireContext(), "Вы уже отправяли приглашение этому пользователю, дождитесь ответа", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "У вас уже есть приглашение от этого пользователя", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Вы уже отправяли приглашение этому пользователю, дождитесь ответа", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        fun newInstance(details: SearchFormsDetails, viewRegime: String) = CompanionFormDetailsFragment(details, viewRegime)
        private const val DRIVER_ROLE = "DRIVER_ROLE"
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val INVITE = 1
        private const val DETAILS = "DETAILS"
        private const val NOTIFICATION = "NOTIFICATION"
    }
}