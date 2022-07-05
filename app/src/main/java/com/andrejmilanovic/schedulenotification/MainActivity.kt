package com.andrejmilanovic.schedulenotification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.andrejmilanovic.schedulenotification.receiver.AlarmReceiver
import com.andrejmilanovic.schedulenotification.ui.component.DatePicker
import com.andrejmilanovic.schedulenotification.ui.component.TimePicker
import com.andrejmilanovic.schedulenotification.ui.theme.ScheduleNotificationTheme
import com.andrejmilanovic.schedulenotification.util.Constants.CHANNEL_ID
import com.andrejmilanovic.schedulenotification.util.Constants.MESSAGE_EXTRA
import com.andrejmilanovic.schedulenotification.util.Constants.NOTIFICATION_ID
import com.andrejmilanovic.schedulenotification.util.Constants.TITLE_EXTRA
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScheduleNotificationTheme {
                var title by remember { mutableStateOf("") }
                var message by remember { mutableStateOf("") }

                val context = LocalContext.current
                val focusManager = LocalFocusManager.current
                val calendar = Calendar.getInstance()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text(text = stringResource(id = R.string.title)) },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text(text = stringResource(id = R.string.message)) },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                        )
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DatePicker(
                                context = context,
                                calendar = calendar,
                                stringResource = stringResource(id = R.string.date_set)
                            )
                            TimePicker(
                                context = context,
                                calendar = calendar,
                                stringResource = stringResource(id = R.string.time_set)
                            )
                        }
                        Button(onClick = { scheduleNotification(title, message, calendar) }) {
                            Text(text = stringResource(id = R.string.schedule))
                        }
                    }
                }

                createChannel(channelName = stringResource(id = R.string.notification_channel_name))
            }
        }
    }

    private fun scheduleNotification(
        title: String,
        message: String,
        calendar: Calendar
    ) {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.putExtra(TITLE_EXTRA, title)
        intent.putExtra(MESSAGE_EXTRA, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val time = calendar.timeInMillis

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        // Show toast message when notification is scheduled
        val date = Date(time)
        val dateFormat = DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = DateFormat.getTimeFormat(applicationContext)
        Toast.makeText(
            applicationContext,
            getString(R.string.notification_scheduled_at) + " " + dateFormat.format(date) + " " + timeFormat.format(
                date
            ),
            Toast.LENGTH_SHORT
        ).show()
    }

    // Create notification channel
    private fun createChannel(channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}