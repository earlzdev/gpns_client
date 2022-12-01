package com.earl.gpns.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    companion object Diff : DiffUtil.ItemCallback<MessageUi>() {
        override fun areItemsTheSame(oldItem: MessageUi, newItem: MessageUi) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: MessageUi, newItem: MessageUi) = oldItem.equals(newItem)
        private const val AUTHOR_VIEW_TYPE = 0
        private const val CONTACTS_VIEW_TYPE = 1
    }
}

abstract class BaseViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(item: MessageUi, isSameUser: Boolean, isSameDate: Boolean) {}

    class AuthorMessage(
        private val binding: RecyclerSenderMessageItemBinding
    ) : BaseViewHolder(binding.root) {
        override fun bind(item: MessageUi, isSameUser: Boolean, isSameDate: Boolean) {
            item.recyclerDetails(binding.messageText, binding.timestamp)
            if (isSameDate) binding.dateHeader.visibility = View.GONE
            else binding.dateHeader.text = item.provideDate()

//        // Omit user profile picture in case of repeated message
//        if (isSameUser && isSameDate) {
//            binding.messageLinearLayout.setBackgroundResource(R.drawable.message_background)
//            binding.userImageImageView.visibility = View.INVISIBLE
//        } else {
//            // loading image URL to imageView
//            ...
//        }
        }
    }

    class ContactMessage(
        private val binding: RecyclerReceiverMessageItemBinding
    ) : BaseViewHolder(binding.root) {
        override fun bind(item: MessageUi, isSameUser: Boolean, isSameDate: Boolean) {
            item.recyclerDetails(binding.messageText, binding.timestamp)
            if (isSameDate) binding.dateHeader.visibility = View.GONE
            else binding.dateHeader.text = item.provideDate()
        }
    }
}

//override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Message) {
//    val isSameUser: Boolean =
//        if (position > 0) model.userId == getItem(position - 1).userId else false
//    val isSameDate: Boolean =
//        if (position > 0) model.date == getItem(position - 1).date else false
//
//    when (viewHolder) {
//        // Based on data I initiated val [viewHolder] with appropriate
//        // enum class member in [getItemViewType()]
//        EnumViewHolder.SenderViewHolder ->
//            (holder as SenderMessageViewHolder).bindData(model, isSameDate, isSameUser)
//        EnumViewHolder.MessageViewHolder ->
//            (holder as MessageViewHolder).bindData(model, isSameDate, isSameUser)
//        else -> false
//    }
//}
//...
//....
//inner class MessageViewHolder(private val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root) {
//    fun bindData(item: Message,
//                 isSameDate: Boolean,
//                 isSameUser: Boolean
//    ) {
//        binding.messageTextTextView.text = item.text
//        binding.messageTimeTextView.text = item.time
//
//        if (isSameDate) {
//            binding.messageDateTextView.visibility = View.GONE
//        } else binding.messageDateTextView.text = item.date
//
//        // Omit user profile picture in case of repeated message
//        if (isSameUser && isSameDate) {
//            binding.messageLinearLayout.setBackgroundResource(R.drawable.message_background)
//            binding.userImageImageView.visibility = View.INVISIBLE
//        } else {
//            // loading image URL to imageView
//            ...
//        }
//    }
//}
//// I did the same for SenderMessageViewHolder()


//class ChatRecyclerAdapter(
//    private val userId: String,
//    private val currentDate: Date
//) : ListAdapter<MessageUi, BaseViewHolder>(Diff) {
//
//    override fun getItemViewType(position: Int) =
//        when(getItem(position).isAuthoredMessage(userId)) {
//            true -> {
//                AUTHOR_VIEW_TYPE
//            }
//            false -> {
//                CONTACTS_VIEW_TYPE
//            }
//        }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
//        AUTHOR_VIEW_TYPE -> {
//            BaseViewHolder.AuthorMessage(
//                RecyclerAuthorMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            )
//        }
//        CONTACTS_VIEW_TYPE -> {
//            BaseViewHolder.ContactMessage(
//                RecyclerContactMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            )
//        }
//        else -> throw IllegalStateException("Unknown view type")
//    }
//
//    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    companion object Diff : DiffUtil.ItemCallback<MessageUi>() {
//        override fun areItemsTheSame(oldItem: MessageUi, newItem: MessageUi) = oldItem.same(newItem)
//        override fun areContentsTheSame(oldItem: MessageUi, newItem: MessageUi) = oldItem.equals(newItem)
//        private const val AUTHOR_VIEW_TYPE = 0
//        private const val CONTACTS_VIEW_TYPE = 1
//        private const val HEADER_TYPE = 2
//    }
//}
//
//abstract class BaseViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
//
//    open fun bind(item: MessageUi) {}
//
//    class AuthorMessage(
//        private val binding: RecyclerAuthorMessageItemBinding
//    ) : BaseViewHolder(binding.root) {
//        override fun bind(item: MessageUi) {
//            item.recyclerDetails(binding.messageText, binding.timestamp)
//        }
//    }
//
//    class ContactMessage(
//        private val binding: RecyclerContactMessageItemBinding
//    ) : BaseViewHolder(binding.root) {
//        override fun bind(item: MessageUi) {
//            item.recyclerDetails(binding.messageText, binding.timestamp)
//        }
//    }
//
//    class DateHeader(
//        private val binding: RecyclerChatHeaderBinding
//    ) : BaseViewHolder(binding.root) {
//        override fun bind(item: MessageUi) {
//
//        }
//    }
//}