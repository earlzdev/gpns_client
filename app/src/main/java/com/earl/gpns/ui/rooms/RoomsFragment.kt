package com.earl.gpns.ui.rooms

import android.os.Bundle
import android.util.Log
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
import com.earl.gpns.core.UpdateLastMessageInRoomCallback
import com.earl.gpns.databinding.FragmentRoomsBinding
import com.earl.gpns.domain.mappers.NewLastMessageInRoomDomainToUiMapper
import com.earl.gpns.domain.models.NewLastMessageInRoomDomain
import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.NewLastMessageInRoomUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RoomsFragment : BaseFragment<FragmentRoomsBinding>(), OnRoomClickListener, UpdateLastMessageInRoomCallback {

    private lateinit var viewModel: RoomsViewModel
    private lateinit var adapter: RoomsRecyclerAdapter
    @Inject
    lateinit var newLastMsgInRoomDomainToUiMapper: NewLastMessageInRoomDomainToUiMapper<NewLastMessageInRoomUi>

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoomsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RoomsViewModel::class.java]
        initSession()
        recycler()
        backPressedCallback()
        binding.testUsername.text = preferenceManager.getString(Keys.KEY_NAME) ?: ""
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

    private fun initSession() {
        navigator.showProgressBar()
        viewModel.initChatSocket(preferenceManager.getString(Keys.KEY_JWT) ?: "", this)
    }

    override fun joinRoom(chatInfo: ChatInfo) {
        navigator.chat(chatInfo)
        adapter.clearCounter(chatInfo)
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
                    adapter.submitList(rooms)
                }
                .collect()
        }
        navigator.hideProgressBar()
    }

    override fun updateLastMessage(newLastMessage: NewLastMessageInRoomDomain) {
        val newLastMsgUi = newLastMessage.mapToUi(newLastMsgInRoomDomainToUiMapper)
        lifecycleScope.launch(Dispatchers.Main) {
            val room = adapter.currentList.find { it.sameId(newLastMsgUi.provideRoomId()) }
            val currentPosition = adapter.currentList.indexOf(room)
            if (room != null) {
                adapter.updateLastMessage(newLastMsgUi.lastMessageForUpdate(), currentPosition)
                adapter.swap(currentPosition)
                if (!newLastMsgUi.isMessageRead()) {
                    adapter.updateCounter(currentPosition)
                }
            }
        }
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