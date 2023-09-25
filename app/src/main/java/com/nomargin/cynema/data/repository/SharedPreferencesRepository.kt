package com.nomargin.cynema.data.repository

interface SharedPreferencesRepository {

    fun putString(key: String, value: String)

    fun getString(key: String, defaultValue: String): String?

}