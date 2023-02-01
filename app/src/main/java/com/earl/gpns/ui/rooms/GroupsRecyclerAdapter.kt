package com.earl.gpns.ui.rooms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.R
import com.earl.gpns.databinding.RecyclerGroupItemBinding
import com.earl.gpns.ui.models.GroupUi
import com.earl.gpns.ui.models.LastMessageForUpdateInGroup

interface OnGroupClickListener {
    fun joinGroup(group: GroupUi)
}

class GroupsRecyclerAdapter(
    private val authorUsername: String,
    private val clickListener: OnGroupClickListener
) : ListAdapter<GroupUi, GroupsRecyclerAdapter.ItemViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RecyclerGroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            clickListener.joinGroup(item)
        }
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    fun updateLastMessage(lstMsg: LastMessageForUpdateInGroup, position: Int) {
        val group = getItem(position)
        group.updateLastMessage(lstMsg)
        notifyItemChanged(position)
    }

    fun markAuthoredMessagesAsRead(groupId: String) {
        val group = currentList.find { it.sameId(groupId) }
        group?.markAuthoredMessagesAsRead()
        notifyItemChanged(currentList.indexOf(group))
    }

    inner class ItemViewHolder(private val binding: RecyclerGroupItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GroupUi) {
            val context = binding.chatImage.context
            item.recyclerDetails(
                authorUsername,
                binding.chatName,
                binding.chatImage,
                binding.chatLastMsg,
                binding.lastMsgAuthor,
                binding.lastMsgAuthorImage,
                binding.timestamp,
                binding.unreadMsgCounter,
                binding.lastMessageUnreadIndicator,
                binding.lastMessageReadIndicator
            )
            if (item.isAuthoredMessage(authorUsername)) {
                binding.lastMsgAuthor.text = context.getString(R.string.you)
                binding.lastMsgAuthorImage.isVisible = false
            }
        }
    }

    companion object Diff : DiffUtil.ItemCallback<GroupUi>() {
        override fun areItemsTheSame(oldItem: GroupUi, newItem: GroupUi) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: GroupUi, newItem: GroupUi) = oldItem.equals(newItem)
    }
}