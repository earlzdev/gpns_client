package com.earl.gpns.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.databinding.RecyclerGroupCompanionItemBinding
import com.earl.gpns.ui.models.UserUi

interface OnCompanionInGroupSettingsClickListener {
    fun removeCompanionFromGroup(username: String)
}

class CompanionGroupUsersRecyclerAdapter(
    private val clickListener: OnCompanionInGroupSettingsClickListener
) : ListAdapter<UserUi, CompanionGroupUsersRecyclerAdapter.ItemViewHolder>(Diff){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RecyclerGroupCompanionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ItemViewHolder(private val binding: RecyclerGroupCompanionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserUi) {
            item.companionGroupRecyclerDetails(
                binding.compImage,
                binding.compName,
                binding.compRole
            )
            binding.removeCompanionFromGroup.setOnClickListener {
                clickListener.removeCompanionFromGroup(item.provideName())
            }
        }
    }

    companion object Diff : DiffUtil.ItemCallback<UserUi>() {
        override fun areItemsTheSame(oldItem: UserUi, newItem: UserUi) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: UserUi, newItem: UserUi) = oldItem.equals(newItem)
    }
}