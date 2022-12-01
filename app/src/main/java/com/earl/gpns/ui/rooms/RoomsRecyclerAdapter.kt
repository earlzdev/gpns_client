package com.earl.gpns.ui.rooms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.databinding.RecyclerChatItemBinding
import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.RoomUi
import com.earl.gpns.ui.models.UserUi

interface OnRoomClickListener {
    fun joinRoom(chatInfo: ChatInfo)
    fun deleteRoom(chatInfo: ChatInfo)
}

class RoomsRecyclerAdapter(
    private val clickListener: OnRoomClickListener
) : ListAdapter<RoomUi, RoomsRecyclerAdapter.ItemViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RecyclerChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ItemViewHolder(private val binding: RecyclerChatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RoomUi) {
            item.recyclerDetails(
                binding.chatImage,
                binding.chatName,
                binding.chatLastMsg
            )
        }
    }

    companion object Diff : DiffUtil.ItemCallback<RoomUi>() {
        override fun areItemsTheSame(oldItem: RoomUi, newItem: RoomUi) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: RoomUi, newItem: RoomUi) = oldItem.equals(newItem)
    }
}