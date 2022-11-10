package com.mmk.timeintervalpicker

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import java.util.concurrent.TimeUnit
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timeIntervalPicker = TimeIntervalPicker.Builder()
            .setTitleText("Time Interval Picker")
            .setIntervalBetweenHours(1)
            .setIntervalBetweenMinutes(5)
            .setHour(3)
            .setMinute(10)
            .setHourListCircular(true)
            .setMinuteListCircular(true)
            .build()


        timeIntervalPicker.addOnPositiveButtonClickListener {
            Log.d(
                "TimePickerValues",
                "hour:${timeIntervalPicker.hour}, " +
                        "minute:${timeIntervalPicker.minute}, " +
                        "timeStamp:${timeIntervalPicker.timeStampInMillis} "
            )
        }

        findViewById<View>(R.id.showTimeIntervalPickerButton).setOnClickListener {
            timeIntervalPicker.show(supportFragmentManager, "TimeIntervalPicker")
        }

    }
}