# TimeIntervalPicker

TimeIntervalPicker is a library that helps developers choose the hour and minute intervals from customized dialog which is not possible with MaterialTimePicker.

## How to add to your project

First, add the JitPack repository to your project gradle file.  

```groovy
repositories {
  maven { url 'https://jitpack.io' }
}
```
Then, add library to your `build.gradle` (app module).

```groovy
implementation 'com.github.mirzemehdi:timeintervalpicker:1.0.0'
```

## Usage
You can customize shown properties in the sample.

```groovy
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
  Log.d("TimePickerValues",
            "hour:${timeIntervalPicker.hour}, " +
            "minute:${timeIntervalPicker.minute}, " +
            "timeStamp:${timeIntervalPicker.timeStampInMillis} "
       )
}

//Show dialog
timeIntervalPicker.show(supportFragmentManager, "TimeIntervalPicker")
       
```
