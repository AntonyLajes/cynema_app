package com.nomargin.cynema.data.repository

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesEditor: SharedPreferences.Editor
) : SharedPreferencesRepository {
    override fun putString(key: String, value: String) {
        sharedPreferencesEditor.putString(key, value)
        sharedPreferencesEditor.apply()
    }

    override fun getString(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }
}