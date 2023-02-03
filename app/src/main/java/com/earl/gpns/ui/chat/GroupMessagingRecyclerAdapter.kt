package com.earl.gpns.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.R
import com.earl.gpns.databinding.RecyclerGroupMessageContactItemBinding
import com.earl.gpns.databinding.RecyclerGroupMessageUserItemBinding
import com.earl.gpns.ui.models.GroupMessageUi

class GroupMessagingRecyclerAdapter(
    private val authorName: String
) : ListAdapter<GroupMessageUi, BaseGroupMessagingViewHolder>(Diff) {

    override fun getItemViewType(position: Int) =
        when(getItem(position).isAuthoredMessage(authorName)) {
            true -> {
                AUTHOR_VIEW_TYPE
            }
            false -> {
                CONTACTS_VIEW_TYPE
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        AUTHOR_VIEW_TYPE -> {
            BaseGroupMessagingViewHolder.AuthorMessage(
                RecyclerGroupMessageUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
        CONTACTS_VIEW_TYPE -> {
            BaseGroupMessagingViewHolder.ContactMessage(
                RecyclerGroupMessageContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
        else -> throw IllegalStateException(parent.context.getString(R.string.unknown_view_type))
    }

    override fun onBindViewHolder(holder: BaseGroupMessagingViewHolder, position: Int) {
        val message = getItem(position)
        val lastPosition = currentList.lastIndex
        val isSameUser: Boolean =
            if (position > 0) message.provideAuthorName() == getItem(position - 1).provideAuthorName() else false
        val isSameDate: Boolean =
            if (position > 0) message.provideDate() == getItem(position - 1).provideDate() else false
        var result = false
        result = if (position != lastPosition) {
            message.provideAuthorName() == getItem(position + 1).provideAuthorName()
        } else if (position == lastPosition) { false } else false
        holder.bind(getItem(position), isSameUser, isSameDate, result)
    }

    fun updateLastMessageAuthorImage() {
        val position = currentList.lastIndex
        val item = getItem(position)
        item.hideAvatar()
        notifyItemChanged(position)
        notifyDataSetChanged()
    }

    fun markMessagesAsRead() {
        currentList.filter { !it.isMessageRead() }.forEach {
            it.markMessageAsRead()
            notifyItemChanged(currentList.indexOf(it))
        }
    }

    companion object Diff : DiffUtil.ItemCallback<GroupMessageUi>() {
        override fun areItemsTheSame(oldItem: GroupMessageUi, newItem: GroupMessageUi) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: GroupMessageUi, newItem: GroupMessageUi) = oldItem.equals(newItem)
        private const val AUTHOR_VIEW_TYPE = 0
        private const val CONTACTS_VIEW_TYPE = 1
    }
}

abstract class BaseGroupMessagingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(item: GroupMessageUi, isSameUser: Boolean, isSameDate: Boolean, lastMessageOfThisAuthor: Boolean) {}

    class AuthorMessage(
        private val binding: RecyclerGroupMessageUserItemBinding
    ) : BaseGroupMessagingViewHolder(binding.root) {
        override fun bind(item: GroupMessageUi, isSameUser: Boolean, isSameDate: Boolean, lastMessageOfThisAuthor: Boolean) {

            item.recyclerDetailsForUser(binding.messageText, binding.timestamp)
            if (isSameDate) binding.dateHeader.visibility = View.GONE
            else { binding.dateHeader.text = item.provideDate() }

            binding.msgUnreadIndicator.isVisible = !item.isMessageRead()
            binding.msgReadIndicator.isVisible = item.isMessageRead()
        }
    }

    class ContactMessage(
        private val binding: RecyclerGroupMessageContactItemBinding
    ) : BaseGroupMessagingViewHolder(binding.root) {
        override fun bind(item: GroupMessageUi, isSameUser: Boolean, isSameDate: Boolean, lastMessageOfThisAuthor: Boolean) {
            item.recyclerDetailsForContact(
                binding.authorName,
                binding.timestamp,
                binding.messageText
            )
            if (isSameDate) binding.dateHeader.visibility = View.GONE else binding.dateHeader.text = item.provideDate()
            if (isSameUser && isSameDate) {
                binding.userAvatar.visibility = View.INVISIBLE
            } else {
                binding.userAvatar.visibility = View.VISIBLE
            }
        }
    }
}