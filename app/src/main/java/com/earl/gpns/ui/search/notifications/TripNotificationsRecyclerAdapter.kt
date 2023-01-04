package com.earl.gpns.ui.search.notifications

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.databinding.RecyclerNotificationItemBinding
import com.earl.gpns.ui.models.TripNotificationRecyclerItemUi

interface NotificationOnClickListener {
    fun showNotificationDetails(id: String, username: String, tripRole: String, watchable: Boolean)
}

class TripNotificationsRecyclerAdapter(
    private val username: String,
    private val tripRole: String,
    private val clickListener: NotificationOnClickListener
) : ListAdapter<TripNotificationRecyclerItemUi, TripNotificationsRecyclerAdapter.ItemViewHolder>(
    Diff
)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RecyclerNotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            if (item.authorName == username && item.authorTripRole == tripRole) {
                clickListener.showNotificationDetails(item.id, item.receiverName, item.receiverTripRole, item.watchable == 1)
            } else {
                clickListener.showNotificationDetails(item.id, item.authorName, item.authorTripRole, item.watchable == 1)
            }
        }
    }

    inner class ItemViewHolder(private val binding: RecyclerNotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TripNotificationRecyclerItemUi) {
            val tripRoleInviter = if (item.authorTripRole == COMPANION_ROLE) "Попутчик" else "Водитель"
            val tripRoleInviting = if (item.receiverTripRole == COMPANION_ROLE) "попутчика" else "водителя"
            Log.d("tag", "bind: $item")
            if (item.type == AGREED && item.receiverName != username) {
                binding.inviteText.text = "Вы приняли приглашение $tripRoleInviting ${item.receiverName} ездить вместе и были добавлены в группу попутчиков, где можно детальнее договориться о поездке."
            } else if (item.type == AGREED && item.receiverName == username) {
                binding.inviteText.text = "$tripRoleInviter ${item.authorName} принял Ваше приглашение ездить вместе. Вы добавлены в совместную группу попутчиков, где можно детальнее договориться о поездке."
            } else if (item.type == DELETED_DRIVER_FORM && item.authorName != username) {
                binding.inviteText.text = "Водитель ${item.authorName}, с которым Вы ездили вместе, удалил свою анкету. Предлагаем найти нового водителя."
            } else if (item.type == DELETED_DRIVER_FORM && item.authorName == username) {
                binding.inviteText.text = "Вы удалили свою анкету водителя."
            } else if (item.type == REMOVED_COMPANION_FROM_GROUP && item.receiverName == username) {
                binding.inviteText.text = "$tripRoleInviter ${item.authorName} убрал Вас из совместной группы попутчиков. Найдите другого водителя"
            } else if (item.type == REMOVED_COMPANION_FROM_GROUP && item.receiverName != username) {
                binding.inviteText.text = "Вы убрали ${item.receiverName} из совместной группы попутчиков"
            } else if (item.type == COMPANION_LEAVED_GROUP && item.receiverName == username) {
                binding.inviteText.text = "$tripRoleInviter ${item.authorName} покинул группу попутчиков"
            } else if (item.type == COMPANION_LEAVED_GROUP && item.receiverName != username) {
                binding.inviteText.text = "Вы покинули группу попутчиков $tripRoleInviting ${item.receiverName}"
            } else {
                    if (item.authorName == username) {
                        binding.inviteText.text = "Вы пригласили $tripRoleInviting ${item.receiverName} ездить вместе."
                    } else {
                        binding.inviteText.text = "$tripRoleInviter ${item.authorName} приглашает Вас ездить вместе."
                    }
                }
            binding.timestamp.text = item.timestamp
            binding.newNotificationIndicator.isVisible = item.read == 0
        }
    }

    companion object Diff : DiffUtil.ItemCallback<TripNotificationRecyclerItemUi>() {
        override fun areItemsTheSame(oldItem: TripNotificationRecyclerItemUi, newItem: TripNotificationRecyclerItemUi) = oldItem.same(newItem)
        override fun areContentsTheSame(oldItem: TripNotificationRecyclerItemUi, newItem: TripNotificationRecyclerItemUi) = oldItem.equals(newItem)
        private const val COMPANION_ROLE = "COMPANION_ROLE"
        private const val DRIVER_ROLE = "DRIVER_ROLE"
        private const val INVITE = "INVITE"
        private const val DELETED_DRIVER_FORM = "DELETED_DRIVER_FORM"
        private const val AGREED = "AGREED"
        private const val REMOVED_COMPANION_FROM_GROUP = "REMOVED_COMPANION_FROM_GROUP"
        private const val COMPANION_LEAVED_GROUP = "COMPANION_LEAVED_GROUP"
    }
}