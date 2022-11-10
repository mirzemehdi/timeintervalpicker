package com.mmk.timeintervalpicker

import androidx.annotation.IntRange
import java.util.concurrent.TimeUnit

internal class TimeValue private constructor(val timeStampInMs: Long, val type: Type) {

    internal enum class Type{
        HOUR,MINUTE
    }

    internal companion object {

        fun ofHours(@IntRange(from = 0, to = 23) value: Int) =
            TimeValue(TimeUnit.HOURS.toMillis(value.toLong()), Type.HOUR)

        fun ofMinutes(@IntRange(from = 0, to = 59) value: Int) =
            TimeValue(TimeUnit.MINUTES.toMillis(value.toLong()), Type.MINUTE)
    }

    val hour get() = TimeUnit.MILLISECONDS.toHours(timeStampInMs).toInt()
    val minutes get() = TimeUnit.MILLISECONDS.toMinutes(timeStampInMs).toInt()

}