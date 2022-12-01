package com.earl.gpns.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T, V : BaseViewHolder<T>>(diff: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, V>(diff) {

    override fun onBindViewHolder(holder: V, position: Int) =
        holder.bind(getItem(position))

    protected fun Int.createView(parent: ViewGroup): View =
        LayoutInflater.from(parent.context).inflate(this, parent, false)
}

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(item: T) {}

//    class Loading<T>(view: View) : BaseViewHolder<T>(view)
//
//    abstract class Clickable<T>(
//        view: View,
//        private val listener: OnItemClickListener<T>
//    ) : BaseViewHolder<T>(view) {
//        override fun bind(item: T) {
//            itemView.setOnClickListener {
//                listener.click(item)
//            }
//        }
//    }
//
//    abstract class Error<T>(view: View) : BaseViewHolder<T>(view) {
//        private val errorTextView = view.findViewById<CustomErrorView>(R.id.view_error)
//        override fun bind(item: T) {
//            errorMessage(item, errorTextView)
//        }
//
//        abstract fun errorMessage(item: T, mapper: TextErrorMapper)
//    }
}