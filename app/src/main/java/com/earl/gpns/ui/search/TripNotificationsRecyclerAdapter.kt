package com.earl.gpns.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.databinding.RecyclerNotificationItemBinding
import com.earl.gpns.ui.models.TripNotificationRecyclerItemUi
import com.earl.gpns.ui.models.TripNotificationUi

interface NotificationOnClickListener {
    fun showNotificationDetails(id: String)
}

class TripNotificationsRecyclerAdapter(
    private val clickListener: NotificationOnClickListener
) : ListAdapter<TripNotificationRecyclerItemUi, TripNotificationsRecyclerAdapter.ItemViewHolder>(Diff)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RecyclerNotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            clickListener.showNotificationDetails(item.id)
        }
    }

    inner class ItemViewHolder(private val binding: RecyclerNotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TripNotificationRecyclerItemUi) {
            val tripRole = if (item.authorTripRole == COMPANION_ROLE) "Попутчик" else "Водитель"
            binding.inviteText.text = "$tripRole ${item.authorName} приглашает Вас ездить вместе"
            binding.timestamp.text = item.timestamp
            binding.newNotificationIndicator.isVisible = item.read == 0
        }
    }

    companion object Diff : DiffUtil.ItemCallback<TripNotificationRecyclerItemUi>() {
        override fun areItemsTheSame(oldItem: TripNotificationRecyclerItemUi, newItem: TripNotificationRecyclerItemUi) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: TripNotificationRecyclerItemUi, newItem: TripNotificationRecyclerItemUi) = oldItem.equals(newItem)
        private const val COMPANION_ROLE = "COMPANION_ROLE"
    }
}