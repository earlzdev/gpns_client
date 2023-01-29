package com.earl.gpns.ui.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
        else -> throw IllegalStateException("Unknown view type")
    }

    override fun onBindViewHolder(holder: BaseGroupMessagingViewHolder, position: Int) {
        val message = getItem(position)
        val lastPosition = currentList.lastIndex
        val isSameUser: Boolean =
            if (position > 0) message.provideAuthorName() == getItem(position - 1).provideAuthorName() else false
        val isSameDate: Boolean =
            if (position > 0) message.provideDate() == getItem(position - 1).provideDate() else false
        val lastMessageOfThisAuthor =
            if (position != lastPosition) message.provideAuthorName() == getItem(position + 1).provideAuthorName() else false
        var result = false
        if (position != lastPosition) {
            result = message.provideAuthorName() == getItem(position + 1).provideAuthorName()
        } else if (position == lastPosition) {
            result = false
        } else {
            result = false
        }
        holder.bind(getItem(position), isSameUser, isSameDate, result)
    }

    fun hideLastAuthorImage(position: Int) {
        notifyItemChanged(position)
    }

    fun updateLastMessageAuthorImage() {
//        val position = currentList.lastIndex
//        Log.d("tag", "updateLastMessageAuthorImage: last idnex -> ${getItem(position).testProvdetext()}")
//        val item = getItem(position)
//        item.hideAvatar()
//        notifyItemChanged(position)
//        notifyDataSetChanged()
    }

    fun update(item: GroupMessageUi) {
        val position = currentList.indexOf(item)
        Log.d("tag", "update: item -> ${item.testProvdetext()}")
        notifyItemChanged(position)
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
//            binding.userAvatar.isVisible = !lastMessageOfThisAuthor
//             Omit user profile picture in case of repeated message
            if (isSameUser && isSameDate) {
                binding.userAvatar.visibility = View.INVISIBLE
//                binding.messageLinearLayout.setBackgroundResource(R.drawable.message_background)
//                binding.userImageImageView.visibility = View.INVISIBLE
            } else {
                binding.userAvatar.visibility = View.VISIBLE
//                 loading image URL to imageView
//                ...
            }
        }
    }
}