package com.nomargin.cynema.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.nomargin.cynema.R

object FrequencyFunctions {

    fun makeToast(context: Context, @StringRes message: Int){
        Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
    }

    fun startNewActivityFromCurrentActivity(currentActivity: Activity, newActivity: Activity){
        val intent = Intent(currentActivity, newActivity::class.java)
        currentActivity.startActivity(intent)
        currentActivity.finish()
    }

}