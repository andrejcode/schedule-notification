package com.andrejmilanovic.schedulenotification.ui.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import java.util.*

@Composable
fun DatePicker(context: Context, calendar: Calendar, stringResource: String) {
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(year, month, day)
            Toast.makeText(context, stringResource, Toast.LENGTH_SHORT).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Button(onClick = { datePickerDialog.show() }) {
        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
    }
}

@Composable
fun TimePicker(context: Context, calendar: Calendar, stringResource: String) {
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            calendar.set(Calendar.HOUR, hour)
            calendar.set(Calendar.MINUTE, minute)
            Toast.makeText(context, stringResource, Toast.LENGTH_SHORT).show()
        },
        calendar.get(Calendar.HOUR),
        calendar.get(Calendar.MINUTE),
        false
    )

    Button(onClick = { timePickerDialog.show() }) {
        Icon(imageVector = Icons.Default.AccessTime, contentDescription = null)
    }
}