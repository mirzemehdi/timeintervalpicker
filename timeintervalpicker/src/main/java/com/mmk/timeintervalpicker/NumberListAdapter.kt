package com.mmk.timeintervalpicker

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

internal class NumberListAdapter(private val isCircularList: Boolean = false) :
    ListAdapter<TimeValue, NumberVH>(ItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberVH {
        return NumberVH.from(parent)
    }

    override fun onBindViewHolder(holder: NumberVH, position: Int) {
        val actualPosition = position % currentList.size
        holder.bind(getItem(actualPosition))
        currentList
    }

    override fun getItemCount(): Int {
        if (isCircularList) return Int.MAX_VALUE
        return super.getItemCount()
    }

    internal class ItemDiffCallback : DiffUtil.ItemCallback<TimeValue>() {
        override fun areItemsTheSame(oldItem: TimeValue, newItem: TimeValue) =
            oldItem.timeStampInMs == newItem.timeStampInMs

        override fun areContentsTheSame(oldItem: TimeValue, newItem: TimeValue) =
            oldItem.timeStampInMs == newItem.timeStampInMs
    }
}


