package com.earl.gpns.ui.search.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.R
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
            val context = binding.inviteText.context
            val tripRoleInviter = if (item.authorTripRole == COMPANION_ROLE) context.getString(R.string.comp) else context.getString(R.string.driver)
            val tripRoleInviting = if (item.receiverTripRole == COMPANION_ROLE) context.getString(R.string.comp_s) else context.getString(R.string.driver_s)
            if (item.type == AGREED && item.receiverName != username) {
                binding.inviteText.text = context.getString(R.string.u_agreed_to_tide_together, tripRoleInviting, item.receiverName)
            } else if (item.type == AGREED && item.receiverName == username) {
                binding.inviteText.text = context.getString(R.string.user_agreed, tripRoleInviter, item.authorName)
            } else if (item.type == DELETED_DRIVER_FORM && item.authorName != username) {
                binding.inviteText.text = context.getString(R.string.ur_driver_deleted_form, item.authorName)
            } else if (item.type == DELETED_DRIVER_FORM && item.authorName == username) {
                binding.inviteText.text = context.getString(R.string.u_deleted_ur_driver_form)
            } else if (item.type == REMOVED_COMPANION_FROM_GROUP && item.receiverName == username) {
                binding.inviteText.text = context.getString(R.string.u_was_deleted_from_comp_group, tripRoleInviter, item.authorName)
            } else if (item.type == REMOVED_COMPANION_FROM_GROUP && item.receiverName != username) {
                binding.inviteText.text = context.getString(R.string.u_deleted_comp_from_group, item.receiverName)
            } else if (item.type == COMPANION_LEAVED_GROUP && item.receiverName == username) {
                binding.inviteText.text = context.getString(R.string.comp_leaved_comp_groupp, tripRoleInviter, item.authorName)
            } else if (item.type == COMPANION_LEAVED_GROUP && item.receiverName != username) {
                binding.inviteText.text = context.getString(R.string.u_leaved_group_of_driver, tripRoleInviting, item.receiverName)
            } else if (item.type == DISAGREED && item.authorTripRole == COMPANION_ROLE && item.receiverName == username) {
                binding.inviteText.text = context.getString(R.string.comp_denied, item.authorName)
            } else if (item.type == DISAGREED && item.authorTripRole == DRIVER_ROLE && item.receiverName == username) {
                binding.inviteText.text = context.getString(R.string.driver_deined, item.authorName)
            } else if (item.type == DISAGREED && item.receiverName != username) {
                binding.inviteText.text = context.getString(R.string.u_denied_to_driver_with, item.receiverName)
            } else {
                if (item.authorName == username) {
                    binding.inviteText.text = context.getString(R.string.u_invited_to_drive, tripRoleInviting, item.receiverName)
                } else {
                    binding.inviteText.text = context.getString(R.string.user_invites_u_to_driver_together, item.authorName)
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
        private const val DELETED_DRIVER_FORM = "DELETED_DRIVER_FORM"
        private const val AGREED = "AGREED"
        private const val REMOVED_COMPANION_FROM_GROUP = "REMOVED_COMPANION_FROM_GROUP"
        private const val COMPANION_LEAVED_GROUP = "COMPANION_LEAVED_GROUP"
        private const val DISAGREED = "DISAGREED"
    }
}