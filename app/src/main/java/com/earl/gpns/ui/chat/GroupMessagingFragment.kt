package com.earl.gpns.ui.chat

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.earl.gpns.R
import com.earl.gpns.databinding.FragmentGroupMessagingBinding
import com.earl.gpns.domain.webSocketActions.services.GroupMessagingSocketActionsService
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.CurrentDateAndTimeGiver
import com.earl.gpns.ui.core.Keys
import com.earl.gpns.ui.models.GroupInfo
import com.earl.gpns.ui.models.GroupMessageUi
import com.earl.gpns.ui.models.GroupTypingStatusUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class GroupMessagingFragment(
    private val groupInfo: GroupInfo
) : BaseFragment<FragmentGroupMessagingBinding>(), GroupMessagingSocketActionsService {

    private lateinit var viewModel: GroupMessagingViewModel
    private lateinit var recyclerAdapter: GroupMessagingRecyclerAdapter
    private lateinit var groupMessangerSocketController: GroupMessangerSocketController
    private var typingStarted = false

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentGroupMessagingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GroupMessagingViewModel::class.java]
        initViews()
        initGroupMessagingSocket()
        recycler()
        initGroupSocketController()
        backPressedCallback()
        typingMessageListener()
        binding.sendMsgBtn.setOnClickListener {
            sendMessageInGroup()
            binding.msgEdittext.text.clear()
        }
        binding.backBtn.setOnClickListener {
            viewModel.closeGroupMessagingSocketConnection()
            navigator.back()
        }
        binding.contactName.setOnClickListener {
            viewModel.deleteGroupMessagesCounter(groupInfo.groupId)
        }
        binding.groupSettings.setOnClickListener {
            navigator.companionGroupSettingsFragment(groupInfo.groupId)
        }
    }

    private fun initViews() {
        binding.contactName.text = groupInfo.title
        if (!groupInfo.isCompanionGroup) {
            binding.groupSettings.visibility = View.GONE
        }
        with(binding.chatImage) {
            if (groupInfo.isCompanionGroup) {
                setImageResource(R.drawable.default_comps_group_image)
            } else {
                setImageResource(R.drawable.default_common_group_image)
            }
        }
    }

    private fun initGroupSocketController() {
        groupMessangerSocketController = GroupMessangerSocketController.Base()
        groupMessangerSocketController.initRecyclerAdapter(recyclerAdapter)
    }

    private fun initGroupMessagingSocket() {
        viewModel.initGroupMessagingSocket(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            groupInfo.groupId,
            this
        )
    }

    private fun recycler() {
        recyclerAdapter = GroupMessagingRecyclerAdapter(preferenceManager.getString(Keys.KEY_NAME) ?: "")
        binding.messagesRecycler.adapter = recyclerAdapter
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.stackFromEnd = true
        binding.messagesRecycler.layoutManager = linearLayoutManager
        lifecycleScope.launchWhenStarted {
            viewModel._messages
                .onEach { messages ->
                    if (messages.isNotEmpty() && !messages.last().isAuthoredMessage(preferenceManager.getString(
                            Keys.KEY_NAME) ?: "")) {
                        val unreadMessagesList = messages.filter { !it.isMessageRead() }
                        if (unreadMessagesList.isNotEmpty()) {
                            markMessagesAsReadInGroup(groupInfo.groupId)
                        }
                    }
                    recyclerAdapter.submitList(messages)
                    binding.messagesRecycler.layoutManager?.smoothScrollToPosition(
                        binding.messagesRecycler,
                        null,
                        messages.size
                    )
                }
                .collect()
        }
    }

    private fun sendMessageInGroup() {
        val messageEntity = GroupMessageUi.Base(
            groupInfo.groupId,
            UUID.randomUUID().toString(),
            preferenceManager.getString(Keys.KEY_NAME) ?: "",
            preferenceManager.getString(Keys.KEY_IMAGE) ?: "",
            CurrentDateAndTimeGiver().fetchCurrentDateAsString(),
            binding.msgEdittext.text.toString().trim(),
            UNREAD
        )
        if (binding.msgEdittext.text.trim().isNotEmpty()) {
            viewModel.increaseReadMessagesCounterInGroup(groupInfo.groupId, 1)
            viewModel.sendMessageInGroup(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                messageEntity
            )
        }
    }

    override fun updateLastMessageAuthorImageInGroup() {
        groupMessangerSocketController.updateLastMessageAuthorImageInGroup()
    }

    override fun updateTypingMessageStatusInGroup(username: String, typingStatus: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (typingStatus == 1) {
                if (username != preferenceManager.getString(Keys.KEY_NAME)) {
                    binding.typingStatus.isVisible = true
                    binding.typingStatus.text = context?.resources?.getString(R.string.user_is_typing, username)
                }
            } else {
                binding.typingStatus.isVisible = false
            }
        }
    }

    override fun markMessagesAsReadInGroup(groupId: String) {
        groupMessangerSocketController.markMessagesAsReadInGroup()
    }

    private fun typingMessageListener() {
        binding.msgEdittext.afterTextChangedDelayed {}
    }

    private fun EditText.afterTextChangedDelayed(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            var timer: CountDownTimer? = null

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!typingStarted) {
                    viewModel.sendGroupTypingMessageStatus(
                        preferenceManager.getString(Keys.KEY_JWT) ?: "",
                        GroupTypingStatusUi.Base(
                            groupInfo.groupId,
                            preferenceManager.getString(Keys.KEY_NAME) ?: "",
                            STARTED_TYPING
                        )
                    )
                    typingStarted = true
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                timer?.cancel()
                timer = object : CountDownTimer(1000, 1500) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        afterTextChanged.invoke(editable.toString())
                        viewModel.sendGroupTypingMessageStatus(
                            preferenceManager.getString(Keys.KEY_JWT) ?: "",
                            GroupTypingStatusUi.Base(
                                groupInfo.groupId,
                                preferenceManager.getString(Keys.KEY_NAME) ?: "",
                                STOPPED_TYPING
                            )
                        )
                        typingStarted = false
                    }
                }.start()
            }
        })
    }

    private fun backPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.closeGroupMessagingSocketConnection()
                navigator.back()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    companion object {

        fun newInstance(groupInfo: GroupInfo) = GroupMessagingFragment(groupInfo)
        private const val STARTED_TYPING = 1
        private const val STOPPED_TYPING = 0
        private const val UNREAD = 0
    }
}

