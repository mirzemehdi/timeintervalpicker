package com.mmk.timeintervalpicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmk.timeintervalpicker.databinding.ItemNumberPickerBinding

internal class NumberVH private constructor(private val binding: ItemNumberPickerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {

        fun from(parent: ViewGroup): NumberVH {
            val layoutInflater = LayoutInflater.from(parent.context)
            val viewBinding = ItemNumberPickerBinding.inflate(layoutInflater, parent, false)
            return NumberVH(viewBinding)
        }
    }

    fun bind(item: TimeValue) {
        val context = binding.root.context
        val text = when (item.type) {
            TimeValue.Type.HOUR -> context.getString(
                R.string.text_time_format_hour,
                getNumberAsString(item.hour)
            )
            TimeValue.Type.MINUTE -> context.getString(
                R.string.text_time_format_minute,
                getNumberAsString(item.minutes)
            )
        }
        binding.textView.text = text
    }

    private fun getNumberAsString(number: Int): String {
        if (number < 10) return "0$number" //Adding extra 0 to number
        return "$number"
    }
}