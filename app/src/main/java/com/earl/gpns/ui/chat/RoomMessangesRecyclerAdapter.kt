package com.earl.gpns.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.databinding.RecyclerReceiverMessageItemBinding
import com.earl.gpns.databinding.RecyclerSenderMessageItemBinding
import com.earl.gpns.ui.models.MessageUi

class ChatRecyclerAdapter(
    private val userId: String
) : ListAdapter<MessageUi, BaseViewHolder>(Diff) {

    override fun getItemViewType(position: Int) =
        when(getItem(position).isAuthoredMessage(userId)) {
            true -> {
                AUTHOR_VIEW_TYPE
            }
            false -> {
                CONTACTS_VIEW_TYPE
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        AUTHOR_VIEW_TYPE -> {
            BaseViewHolder.AuthorMessage(
                RecyclerSenderMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
        CONTACTS_VIEW_TYPE -> {
            BaseViewHolder.ContactMessage(
                RecyclerReceiverMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
        else -> throw IllegalStateException("Unknown view type")
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val message = getItem(position)
        val isSameUser: Boolean =
            if (position > 0) message.provideAuthorId() == getItem(position - 1).provideAuthorId() else false
        val isSameDate: Boolean =
            if (position > 0) message.provideDate() == getItem(position - 1).provideDate() else false
        holder.bind(getItem(position), isSameUser, isSameDate)
    }

    fun markMessagesAsRead() {
        currentList.filter { !it.isMessageRead() }.forEach {
            it.markMessageAsRead()
            notifyItemChanged(currentList.indexOf(it))
        }
    }

    companion object Diff : DiffUtil.ItemCallback<MessageUi>() {
        override fun areItemsTheSame(oldItem: MessageUi, newItem: MessageUi) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: MessageUi, newItem: MessageUi) = oldItem.equals(newItem)
        private const val AUTHOR_VIEW_TYPE = 0
        private const val CONTACTS_VIEW_TYPE = 1
    }
}

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(item: MessageUi, isSameUser: Boolean, isSameDate: Boolean) {}

    class AuthorMessage(
        private val binding: RecyclerSenderMessageItemBinding
    ) : BaseViewHolder(binding.root) {
        override fun bind(item: MessageUi, isSameUser: Boolean, isSameDate: Boolean) {
            item.recyclerDetails(binding.messageText, binding.timestamp)
            if (isSameDate) binding.dateHeader.visibility = View.GONE
            else { binding.dateHeader.text = item.provideDate() }

            binding.msgUnreadIndicator.isVisible = !item.isMessageRead()
            binding.msgReadIndicator.isVisible = item.isMessageRead()
        }
    }

    class ContactMessage(
        private val binding: RecyclerReceiverMessageItemBinding
    ) : BaseViewHolder(binding.root) {
        override fun bind(item: MessageUi, isSameUser: Boolean, isSameDate: Boolean) {
            item.recyclerDetails(binding.messageText, binding.timestamp)
            if (isSameDate) binding.dateHeader.visibility = View.GONE
            else { binding.dateHeader.text = item.provideDate() }
        }
    }
}