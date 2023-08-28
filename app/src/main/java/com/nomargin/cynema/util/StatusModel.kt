package com.nomargin.cynema.util

import androidx.annotation.StringRes

data class StatusModel(
    val isValid: Boolean = false,
    val errorType: Int? = null,
    @StringRes
    val message: Int
)
