package com.nomargin.cynema.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.google.firebase.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object FrequencyFunctions {

    fun makeToast(context: Context, @StringRes message: Int) {
        Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
    }

    fun startNewActivityFromCurrentActivity(currentActivity: Activity, newActivity: Activity) {
        val intent = Intent(currentActivity, newActivity::class.java)
        currentActivity.startActivity(intent)
        currentActivity.finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPostDifferenceTime(timestamp: Timestamp?): String {
        val postDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp!!.seconds, 0),
            ZoneId.systemDefault()
        )
        val currentDateTime = LocalDateTime.now()
        val diff = Duration.between(postDateTime, currentDateTime)
        return when {
            diff.toDays() >= 1 -> {
                if (diff.toDays().toInt() >= 365) {
                    buildString {
                        append(
                            postDateTime
                                .month.name.lowercase().take(3)
                                .replaceFirstChar { firstChar ->
                                    firstChar.uppercase()
                                }
                        )
                        append(" ")
                        append(postDateTime.dayOfMonth)
                        append(", ")
                        append(postDateTime.year)
                    }
                } else {
                    buildString {
                        append(
                            postDateTime
                                .month.name.lowercase().take(3)
                                .replaceFirstChar { firstChar ->
                                    firstChar.uppercase()
                                }
                        )
                        append(" ")
                        append(postDateTime.dayOfMonth)
                    }
                }
            }

            diff.toHours() >= 1 -> {
                "${diff.toHours()}hr"
            }

            else -> {
                "${diff.toMinutes()}m"
            }
        }
    }

}