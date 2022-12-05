package com.earl.gpns.ui.rooms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.databinding.RecyclerRoomItemBinding
import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.LastMessageForUpdate
import com.earl.gpns.ui.models.RoomUi
import java.util.*

interface OnRoomClickListener {
    fun joinRoom(chatInfo: ChatInfo)
    fun deleteRoom(chatInfo: ChatInfo)
}

class RoomsRecyclerAdapter(
    private val clickListener: OnRoomClickListener
) : ListAdapter<RoomUi, RoomsRecyclerAdapter.ItemViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RecyclerRoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            clickListener.joinRoom(item.chatInfo())
        }
        holder.itemView.setOnLongClickListener {
            clickListener.deleteRoom(item.chatInfo())
            true
        }
    }

    fun updateLastMessage(messageForUpdate: LastMessageForUpdate,  position: Int) {
        val item = getItem(position)
        item.updateLastMessage(messageForUpdate)
        notifyItemChanged(position)
    }

    fun swap(position: Int) {
        val list = currentList.toMutableList()
        Collections.swap(list, position, STAT_POSITION)
        this.submitList(list)
    }

    fun updateCounter(position: Int) {
        val item = getItem(position)
        item.updateUnreadMsgCount()
        notifyItemChanged(position)
    }

    fun clearCounter(chatInfo: ChatInfo) {
        val item = currentList.toMutableList().find { it.sameId(chatInfo.roomId ?: "") }
        val position = currentList.indexOf(item)
        item?.clearUnreadMsgCounter()
        notifyItemChanged(position)
    }

    inner class ItemViewHolder(private val binding: RecyclerRoomItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RoomUi) {
            item.recyclerDetails(
                binding.chatImage,
                binding.chatName,
                binding.chatLastMsg,
                binding.unreadMsgCounter
            )
            binding.lastMsgAuthor.isVisible = !item.messageBelongsSender()
            binding.unreadMsgCounter.isVisible = !item.isUnreadMsgCountNull()
        }
    }

    companion object Diff : DiffUtil.ItemCallback<RoomUi>() {
        override fun areItemsTheSame(oldItem: RoomUi, newItem: RoomUi) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: RoomUi, newItem: RoomUi) = oldItem.equals(newItem)
        private const val STAT_POSITION = 0
    }
}