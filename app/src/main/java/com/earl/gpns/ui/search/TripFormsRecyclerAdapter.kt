package com.earl.gpns.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.earl.gpns.databinding.RecyclerSearchItemBinding
import com.earl.gpns.ui.SearchFormsDetails
import com.earl.gpns.ui.models.TripFormUi

interface OnSearchFormClickListener {
    fun showDetails(role: String, details: SearchFormsDetails)
}

class TripFormsRecyclerAdapter(
    private val clickListener: OnSearchFormClickListener
) : ListAdapter<TripFormUi, TripFormsRecyclerAdapter.ItemViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RecyclerSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ItemViewHolder(private val binding: RecyclerSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TripFormUi) {
            item.recyclerDetails(
                binding.userName,
                binding.userAvatar,
                binding.tripRole,
                binding.driverIcon,
                binding.companionIcon,
                binding.fromEd,
                binding.driveToEd,
                binding.schduleEd
            )
            binding.lookTripDetailsBtn.setOnClickListener {
                clickListener.showDetails(item.provideTripRole(), item.provideDetails())
            }
        }
    }

    companion object Diff : DiffUtil.ItemCallback<TripFormUi>() {
        override fun areItemsTheSame(oldItem: TripFormUi, newItem: TripFormUi) =
            oldItem.same(newItem)

        override fun areContentsTheSame(oldItem: TripFormUi, newItem: TripFormUi) =
            oldItem.equals(newItem)
    }
}