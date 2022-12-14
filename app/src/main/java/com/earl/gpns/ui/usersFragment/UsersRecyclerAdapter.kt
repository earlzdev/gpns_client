package com.earl.gpns.ui.usersFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.databinding.RecyclerUserItemBinding
import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.models.UserUi

interface UserClickListener {
    fun joinChat(chatInfo: ChatInfo)
}

class UsersRecyclerAdapter(
    private val clickListener: UserClickListener
) : ListAdapter<UserUi, UsersRecyclerAdapter.ItemViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RecyclerUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            clickListener.joinChat(item.chatInfo())
        }
    }

    inner class ItemViewHolder(private val binding: RecyclerUserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserUi) {
            item.recyclerDetails(binding.userAvatar, binding.userName, binding.userLastSeen, binding.onlineIndicator)
        }
    }

    companion object Diff : DiffUtil.ItemCallback<UserUi>() {
        override fun areItemsTheSame(oldItem: UserUi, newItem: UserUi) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: UserUi, newItem: UserUi) = oldItem.equals(newItem)
    }
}