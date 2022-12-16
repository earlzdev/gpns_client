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
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentRoomsBinding
import com.earl.gpns.domain.mappers.NewLastMessageInRoomDomainToUiMapper
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.domain.webSocketActions.services.RoomsObservingSocketService
import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.NewLastMessageInRoomUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class RoomsFragment : BaseFragment<FragmentRoomsBinding>(), OnRoomClickListener,
    RoomsObservingSocketService {

    private lateinit var viewModel: RoomsViewModel
    private lateinit var adapter: RoomsRecyclerAdapter
    @Inject
    lateinit var roomController: RoomsObservingSocketController
    @Inject
    lateinit var newLastMsgInRoomDomainToUiMapper: NewLastMessageInRoomDomainToUiMapper<NewLastMessageInRoomUi>

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoomsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RoomsViewModel::class.java]
        initSession()
        initClickListeners()
        recycler()
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
        roomController = RoomsObservingSocketController.Base(newLastMsgInRoomDomainToUiMapper)
        roomController.setPreferenceManager(preferenceManager)
        roomController.setRecyclerAdapter(adapter)
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
        adapter.clearCounter(chatInfo.roomId ?: "")
        viewModel.joinRoom(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            chatInfo
        )
//        viewModel.markAuthoredMessageAsRead(
//            preferenceManager.getString(Keys.KEY_JWT) ?: "",
//            chatInfo.roomId ?: "",
//            chatInfo.chatTitle
//        )
//        viewModel.updateLastMsgReadState(
//            preferenceManager.getString(Keys.KEY_JWT) ?: "",
//            chatInfo.roomId ?: ""
//        )
    }

    override fun deleteRoom(chatInfo: ChatInfo) {
        viewModel.deleteRoom(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            chatInfo.roomId!!
        )
        Toast.makeText(requireContext(), getString(R.string.room_with_this_user_successfully_removed), Toast.LENGTH_SHORT).show()
    }

    private fun recycler() {
        adapter = RoomsRecyclerAdapter(this)
        binding.chatRecycler.adapter = adapter
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
                    adapter.submitList(rooms)
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

    override fun updateUserOnlineInRoomObserving(
        roomId: String,
        online: Int,
        lastAuthDate: String
    ) {
        roomController.updateUserOnlineInRoomObserving(roomId, online, lastAuthDate)
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