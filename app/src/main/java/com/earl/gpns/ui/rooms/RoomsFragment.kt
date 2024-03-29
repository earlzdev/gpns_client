package com.earl.gpns.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.earl.gpns.R
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.Keys
import com.earl.gpns.databinding.FragmentRoomsBinding
import com.earl.gpns.domain.mappers.GroupDomainToUiMapper
import com.earl.gpns.domain.mappers.GroupLastMessageDomainToUiMapper
import com.earl.gpns.domain.mappers.NewLastMessageInRoomDomainToUiMapper
import com.earl.gpns.domain.models.GroupDomain
import com.earl.gpns.domain.models.GroupLastMessageDomain
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.domain.webSocketActions.services.RoomsObservingSocketService
import com.earl.gpns.ui.models.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class RoomsFragment : BaseFragment<FragmentRoomsBinding>(), OnRoomClickListener, RoomsObservingSocketService, OnGroupClickListener {

    private lateinit var viewModel: RoomsViewModel
    private lateinit var roomsRecyclerAdapter: RoomsRecyclerAdapter
    private lateinit var groupsRecyclerAdapter: GroupsRecyclerAdapter
    @Inject
    lateinit var roomController: RoomsObservingSocketController
    @Inject
    lateinit var newLastMsgInRoomDomainToUiMapper: NewLastMessageInRoomDomainToUiMapper<NewLastMessageInRoomUi>
    @Inject
    lateinit var groupLastMessageDomainToUiMapper: GroupLastMessageDomainToUiMapper<GroupLastMessageUi>
    @Inject
    lateinit var groupDomainToUiMapper: GroupDomainToUiMapper<GroupUi>

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoomsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RoomsViewModel::class.java]
        initSession()
        initClickListeners()
        groupsRecycler()
        roomRecycler()
        initRoomController()
        backPressedCallback()
        binding.testUsername.text = preferenceManager.getString(Keys.KEY_NAME) // todo don't forget to remove then...
    }

    private fun initClickListeners() {
        binding.newMsgBtn.setOnClickListener {
            navigator.showProgressBar()
            navigator.usersFragment()
        }
        binding.chatChapter.setOnClickListener {
            // todo refactor !!!
            viewModel.clearDatabase()
            Toast.makeText(requireContext(), "Database cleared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRoomController() {
        roomController = RoomsObservingSocketController.Base(newLastMsgInRoomDomainToUiMapper, groupLastMessageDomainToUiMapper, groupDomainToUiMapper)
        roomController.setPreferenceManager(preferenceManager)
        roomController.setRoomsRecyclerAdapter(roomsRecyclerAdapter)
        roomController.setGroupsRecyclerAdapter(groupsRecyclerAdapter)
        roomController.setViewModel(viewModel)
    }

    private fun initSession() {
        navigator.showProgressBar()
        viewModel.initChatSocket(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            this
        )
    }

    override fun joinRoom(chatInfo: ChatInfo) {
        navigator.chat(chatInfo)
        roomsRecyclerAdapter.clearCounter(chatInfo.roomId ?: "")
        if (chatInfo.lastMsgAuthor != preferenceManager.getString(Keys.KEY_NAME)) {
            viewModel.joinRoom(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                chatInfo
            )
        }
    }

    override fun deleteRoom(chatInfo: ChatInfo) {
        viewModel.deleteRoom(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            chatInfo.roomId!!
        )
        Toast.makeText(requireContext(), getString(R.string.room_with_this_user_successfully_removed), Toast.LENGTH_SHORT).show()
    }

    private fun groupsRecycler() {
        groupsRecyclerAdapter = GroupsRecyclerAdapter(
            preferenceManager.getString(Keys.KEY_NAME) ?: "",
            this
        )
        binding.groupsRecycler.adapter = groupsRecyclerAdapter
        lifecycleScope.launchWhenStarted {
            viewModel._groups
                .onEach {
                        groups ->
                    if (groups.isNotEmpty()) {
                        groupsRecyclerAdapter.submitList(groups)
                    }
                }
                .collect()
        }
    }

    private fun roomRecycler() {
        roomsRecyclerAdapter = RoomsRecyclerAdapter(this)
        binding.chatRecycler.adapter = roomsRecyclerAdapter
        lifecycleScope.launchWhenStarted {
            viewModel._rooms
                .onEach {
                        rooms ->
                    if (rooms.isNotEmpty()) {
                        rooms.forEach {
                            if (it.isLastMessageAuthorEqualsCurrentUser() && !it.isLastMsgRead()) {
                                it.showUnreadMsgIndicator()
                                it.clearUnreadMsgCounter()
                            }
                        }
                    }
                    roomsRecyclerAdapter.submitList(rooms)
                }
                .collect()
        }
        navigator.hideProgressBar()
    }

    override fun removeDeletedByAnotherUserRoomFromDb(roomId: String, contactName: String) {
        roomController.removeDeletedByAnotherUserRoomFromDb(roomId, contactName)
    }

    override fun updateLastMessageInRoom(newLastMessage: NewLastMessageInRoomDomain) {
        roomController.updateLastMessageInRoom(newLastMessage)
    }

    override fun updateLastMessageInRoomReadState(roomId: String) {
        roomController.updateLastMessageInRoomReadState(roomId)
    }

    override fun updateUserOnlineInRoomObserving(roomId: String, online: Int, lastAuthDate: String) {
        roomController.updateUserOnlineInRoomObserving(roomId, online, lastAuthDate)
    }

    override fun updateLastMessageInGroup(newLastMessageDomain: GroupLastMessageDomain) {
        roomController.updateLastMessageInGroup(newLastMessageDomain)
    }

    override fun addNewGroup(group: GroupDomain) {
        viewModel.insertNewGroup(group.mapToUi(groupDomainToUiMapper))
    }

    override fun joinGroup(group: GroupUi) {
        viewModel.increaseMessagesReadCounterInGroup(group.provideId(), group.provideGroupMessagesCounter())
        val groupInfo = group.provideGroupInfo()
        if (groupInfo.lastMessageAuthor != preferenceManager.getString(Keys.KEY_NAME)) {
            viewModel.markMessagesAsReadInGroup(
                preferenceManager.getString(Keys.KEY_JWT) ?: "",
                groupInfo.groupId
            )
            groupsRecyclerAdapter.markAuthoredMessagesAsRead(groupInfo.groupId)
        }
        navigator.groupMessaging(groupInfo)
    }

    override fun markAuthoredMessagesAsReadInGroup(groupId: String) {
        roomController.markAuthoredMessagesAsReadInGroup(groupId)
    }

    override fun removeDeletedGroup(group: GroupDomain) {
        viewModel.removeDeletedGroup(group.mapToUi(groupDomainToUiMapper))
    }

    private fun backPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigator.exit()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    companion object {

        fun newInstance() = RoomsFragment()
    }
}