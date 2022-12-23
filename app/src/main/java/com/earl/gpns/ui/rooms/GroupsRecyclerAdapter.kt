package com.earl.gpns.ui.rooms

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.R
import com.earl.gpns.databinding.RecyclerGroupItemBinding
import com.earl.gpns.ui.models.GroupInfo
import com.earl.gpns.ui.models.GroupUi
import com.earl.gpns.ui.models.LastMessageForUpdateInGroup

interface OnGroupClickListener {
    fun joinGroup(groupInfo: GroupInfo)
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
            clickListener.joinGroup(item.provideGroupInfo())
        }
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    fun updateGroupByCallback(new: List<GroupUi>) {
        val callback = GroupDiffUtil(currentList, new)
        val result = DiffUtil.calculateDiff(callback)
//        currentList.toMutableList().clear()
//        currentList.toMutableList().addAll(new)
        result.dispatchUpdatesTo(this)
    }

    fun updateLastMessage(lstMsg: LastMessageForUpdateInGroup, position: Int) {
        val group = getItem(position)
        group.updateLastMessage(lstMsg)
        notifyItemChanged(position)
    }

    fun updateGroup(groupId: String) {
        val group = currentList.find { it.sameId(groupId) }
        notifyItemChanged(currentList.indexOf(group))
    }

    fun markAuthoredMessagesAsRead(groupId: String) {
        val group = currentList.find { it.sameId(groupId) }
        group?.markAuthoredMessagesAsRead()
        notifyItemChanged(currentList.indexOf(group))
    }

    inner class ItemViewHolder(private val binding: RecyclerGroupItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GroupUi) {
            if (binding.lastMsgAuthorImage.isVisible) {
                Log.d("tag", "bind: lastMsgAuthorImage visible")
            } else {
                Log.d("tag", "bind: lastMsgAuthorImage invisible")
            }
            if (binding.unreadMsgCounter.isVisible) {
                Log.d("tag", "bind: unreadMsgCounter visible")
            } else {
                Log.d("tag", "bind: unreadMsgCounter invisible")
            }
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

class GroupDiffUtil(
    private val old: List<GroupUi>,
    private val new: List<GroupUi>
) : DiffUtil.Callback() {

    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val result = old[oldItemPosition].same(new[newItemPosition])
        Log.d("tag", "areItemsTheSame: old counter${old.last().provideGroupMessagesCounter()}")
        Log.d("tag", "areItemsTheSame: new counter ${new.last().provideGroupMessagesCounter()}")
        Log.d("tag", "areItemsTheSame: $result")
        return result
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val result = old[oldItemPosition] == new[newItemPosition]
        Log.d("tag", "areContentsTheSame: old counter${old.last().provideGroupMessagesCounter()}")
        Log.d("tag", "areContentsTheSame: new counter ${new.last().provideGroupMessagesCounter()}")
        Log.d("tag", "areContentsTheSame: $result")
        return result
    }

    //    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
//        old[oldItemPosition].same(new[newItemPosition])
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
//        old[oldItemPosition] == new[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        Log.d("tag", "getChangePayload: payload")
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}