package com.mmk.timeintervalpicker

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.IntRange
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mmk.timeintervalpicker.databinding.TimeIntervalPickerDialogBinding
import java.util.concurrent.TimeUnit

class TimeIntervalPicker : DialogFragment() {

    private var _binding: TimeIntervalPickerDialogBinding? = null
    private val binding get() = _binding!!

    private var isHourListCircular = DEFAULT_IS_LIST_CIRCULAR
    private var isMinuteListCircular = DEFAULT_IS_LIST_CIRCULAR
    private var titleText: String = DEFAULT_TITLE_TEXT
    private var intervalBetweenHours: Int = DEFAULT_INTERVAL_BETWEEN_HOURS
    private var intervalBetweenMinutes: Int = DEFAULT_INTERVAL_BETWEEN_MINUTES

    private val hourListAdapter by lazy { NumberListAdapter(isHourListCircular) }
    private val minuteListAdapter by lazy { NumberListAdapter(isMinuteListCircular) }

    private val hourLinearSnapHelper by lazy { LinearSnapHelper() }
    private val minuteLinearSnapHelper by lazy { LinearSnapHelper() }

    private var onPositiveButtonClick: View.OnClickListener? = null
    private var onNegativeButtonClick: View.OnClickListener? = null


    /**
     * Hour value that is set.
     */
    var hour: Int = 0

    /**
     * Minute value that is set.
     */
    var minute: Int = 0

    /**
     * Timestamp value in milliseconds that is calculated from hour and minute values.
     */
    val timeStampInMillis
        get() = TimeUnit.HOURS.toMillis(hour.toLong()) + TimeUnit.MINUTES.toMillis(
            minute.toLong()
        )

    companion object {
        private val TAG = TimeIntervalPicker::class.java.canonicalName
        private const val EXTRA_INTERVAL_BETWEEN_HOURS = "EXTRA_INTERVAL_BETWEEN_HOURS"
        private const val EXTRA_INTERVAL_BETWEEN_MINUTES = "EXTRA_INTERVAL_BETWEEN_MINUTES"
        private const val EXTRA_TITLE_TEXT = "EXTRA_TITLE_TEXT"
        private const val EXTRA_HOUR = "EXTRA_HOUR"
        private const val EXTRA_MINUTE = "EXTRA_MINUTE"
        private const val EXTRA_IS_HOUR_LIST_CIRCULAR = "EXTRA_IS_HOUR_LIST_CIRCULAR"
        private const val EXTRA_IS_MINUTE_LIST_CIRCULAR = "EXTRA_IS_MINUTE_LIST_CIRCULAR"

        private const val DEFAULT_TITLE_TEXT = ""
        private const val DEFAULT_INTERVAL_BETWEEN_HOURS = 1
        private const val DEFAULT_INTERVAL_BETWEEN_MINUTES = 1
        private const val DEFAULT_HOUR_VALUE = 0
        private const val DEFAULT_MINUTE_VALUE = 0
        private const val DEFAULT_IS_LIST_CIRCULAR = false

        private fun newInstance(builder: Builder): TimeIntervalPicker {
            val timeIntervalPickerDialogFragment = TimeIntervalPicker()
            val args = bundleOf(
                EXTRA_INTERVAL_BETWEEN_HOURS to builder.intervalBetweenHours,
                EXTRA_INTERVAL_BETWEEN_MINUTES to builder.intervalBetweenMinutes,
                EXTRA_TITLE_TEXT to builder.titleText,
                EXTRA_HOUR to builder.hour,
                EXTRA_MINUTE to builder.minute,
                EXTRA_IS_HOUR_LIST_CIRCULAR to builder.isHourListCircular,
                EXTRA_IS_MINUTE_LIST_CIRCULAR to builder.isMinuteListCircular
            )
            timeIntervalPickerDialogFragment.arguments = args
            return timeIntervalPickerDialogFragment
        }

        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_INTERVAL_BETWEEN_HOURS, intervalBetweenHours)
        outState.putInt(EXTRA_INTERVAL_BETWEEN_MINUTES, intervalBetweenMinutes)
        outState.putInt(EXTRA_HOUR, hour)
        outState.putInt(EXTRA_MINUTE, minute)
        outState.putString(EXTRA_TITLE_TEXT, titleText)
        outState.putBoolean(EXTRA_IS_HOUR_LIST_CIRCULAR, isHourListCircular)
        outState.putBoolean(EXTRA_IS_MINUTE_LIST_CIRCULAR, isMinuteListCircular)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = savedInstanceState ?: arguments
        args?.let {
            titleText = args.getString(EXTRA_TITLE_TEXT, DEFAULT_TITLE_TEXT)
            intervalBetweenHours =
                args.getInt(EXTRA_INTERVAL_BETWEEN_HOURS, DEFAULT_INTERVAL_BETWEEN_HOURS)
            intervalBetweenMinutes =
                args.getInt(EXTRA_INTERVAL_BETWEEN_MINUTES, DEFAULT_INTERVAL_BETWEEN_MINUTES)
            hour = args.getInt(EXTRA_HOUR, DEFAULT_HOUR_VALUE)
            minute = args.getInt(EXTRA_MINUTE, DEFAULT_MINUTE_VALUE)
            isHourListCircular =
                args.getBoolean(EXTRA_IS_HOUR_LIST_CIRCULAR, DEFAULT_IS_LIST_CIRCULAR)
            isMinuteListCircular =
                args.getBoolean(EXTRA_IS_MINUTE_LIST_CIRCULAR, DEFAULT_IS_LIST_CIRCULAR)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = TimeIntervalPickerDialogBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialog).apply {
            setView(binding.root)
        }.create()
        dialog.window?.let {
            it.requestFeature(Window.FEATURE_NO_TITLE)
            it.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        initViews()
        setClicks()
        return dialog
    }


    private fun initViews() {
        binding.hourNumberPicker.adapter = hourListAdapter
        binding.minuteNumberPicker.adapter = minuteListAdapter
        binding.titleTv.text = titleText
        hourLinearSnapHelper.attachToRecyclerView(binding.hourNumberPicker)
        minuteLinearSnapHelper.attachToRecyclerView(binding.minuteNumberPicker)
        setupList()
    }

    private fun setClicks() {
        binding.okButton.setOnClickListener {
            setupHourAndMinuteValues()
            onPositiveButtonClick?.onClick(it)
            dismiss()
        }
        binding.cancelButton.setOnClickListener {
            onNegativeButtonClick?.onClick(it)
            dismiss()
        }
    }

    private fun setupHourAndMinuteValues() {
        val hourLayoutManager = binding.hourNumberPicker.layoutManager
        val hourSnappedView = hourLinearSnapHelper.findSnapView(hourLayoutManager)
        hourSnappedView?.let {
            val list = hourListAdapter.currentList
            val itemPosition = (hourLayoutManager?.getPosition(it) ?: 0) % list.size
            hour = list[itemPosition].hour
        }

        val minuteLayoutManager = binding.minuteNumberPicker.layoutManager
        val minuteSnappedView = minuteLinearSnapHelper.findSnapView(minuteLayoutManager)
        minuteSnappedView?.let {
            val list = minuteListAdapter.currentList
            val itemPosition = (minuteLayoutManager?.getPosition(it) ?: 0) % list.size
            minute = list[itemPosition].minutes
        }
    }

    private fun setupList() {
        val hourList = (0..23 step intervalBetweenHours).map { TimeValue.ofHours(it) }
        val foundedHourPosition = hourList.indexOfFirst { it.hour == hour }
        val hourIndexPosition =
            if (foundedHourPosition == -1) getCenterIndexPosition(hourList.size)
            else foundedHourPosition + getCenterIndexPosition(hourList.size)

        hourListAdapter.submitList(hourList)
        binding.hourNumberPicker.scrollToPosition(hourIndexPosition)

        val minuteList = (0..59 step intervalBetweenMinutes).map { TimeValue.ofMinutes(it) }
        val foundedMinutePosition = minuteList.indexOfFirst { it.minutes == minute }
        val minuteIndexPosition =
            if (foundedMinutePosition == -1) getCenterIndexPosition(minuteList.size)
            else foundedMinutePosition + getCenterIndexPosition(minuteList.size)
        minuteListAdapter.submitList(minuteList)
        binding.minuteNumberPicker.scrollToPosition(minuteIndexPosition)
    }

    private fun getCenterIndexPosition(listSize: Int) =
        (Int.MAX_VALUE / 2) - (Int.MAX_VALUE / 2) % listSize


    override fun onDismiss(dialog: DialogInterface) {
        _binding = null
        super.onDismiss(dialog)
    }

    /**
     * Listener is called when user clicks ok button.
     */
    fun addOnPositiveButtonClickListener(listener: View.OnClickListener) {
        onPositiveButtonClick = listener
    }

    /**
     * Listener is called when user clicks cancel button.
     */
    fun addOnNegativeButtonClickListener(listener: View.OnClickListener) {
        onNegativeButtonClick = listener
    }


    class Builder {

        var intervalBetweenMinutes = DEFAULT_INTERVAL_BETWEEN_MINUTES
            private set
        var intervalBetweenHours = DEFAULT_INTERVAL_BETWEEN_HOURS
            private set

        /**
         * Title text that is shown in top of dialog.
         */
        var titleText = DEFAULT_TITLE_TEXT
            private set

        /**
         * Current hour value.
         */
        var hour = DEFAULT_HOUR_VALUE
            private set

        /**
         * Current minute value.
         */
        var minute = DEFAULT_MINUTE_VALUE
            private set

        /**
         * Boolean value that decides whether hour list
         * can be scrolled in both direction infinitely.
         */
        var isHourListCircular = DEFAULT_IS_LIST_CIRCULAR
            private set

        /**
         * Boolean value that decides whether minute list
         * can be scrolled in both direction infinitely.
         */
        var isMinuteListCircular = DEFAULT_IS_LIST_CIRCULAR
            private set

        /**
         * Sets interval between minutes in list. By default value is
         * @see DEFAULT_INTERVAL_BETWEEN_MINUTES
         */
        fun setIntervalBetweenMinutes(value: Int) = apply { intervalBetweenMinutes = value }

        /**
         * Sets interval between hours in list. By default value is
         * @see DEFAULT_INTERVAL_BETWEEN_HOURS
         */
        fun setIntervalBetweenHours(value: Int) = apply { intervalBetweenHours = value }

        /**
         * Sets current hour value that can be in range [0,23]. By default value is
         * @see DEFAULT_HOUR_VALUE
         */
        fun setHour(@IntRange(from = 0, to = 23) value: Int) = apply { hour = value }

        /**
         * Sets current minute value that can be in range [0,59]. By default value is
         * @see DEFAULT_MINUTE_VALUE
         */
        fun setMinute(@IntRange(from = 0, to = 59) value: Int) = apply { minute = value }

        /**
         * Sets title that's shown in top of dialog. By default it is empty.
         */
        fun setTitleText(value: String) = apply { titleText = value }

        /**
         *  @param isCircular - if value is true, then hour list will be circular that can be
         *  scrolled in both direction. By default it is not circular.
         */
        fun setHourListCircular(isCircular: Boolean) = apply { isHourListCircular = isCircular }

        /**
         *  @param isCircular - if value is true, then minute list will be circular that can be
         *  scrolled in both direction. By default it is not circular.
         */
        fun setMinuteListCircular(isCircular: Boolean) = apply { isMinuteListCircular = isCircular }

        /**
         * Builds dialog that can be shown using
         * @see show method.
         */
        fun build() = newInstance(this)
    }

}