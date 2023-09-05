package com.nomargin.cynema.util.extension

import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout


object TextInputLayoutExtensions {
    fun TextInputLayout.setFieldError(@StringRes error: Int?) {
        this.error = if (error != null) {
            this.isErrorEnabled = true
            context.getString(error)
        } else {
            this.isErrorEnabled = false
            null
        }
    }
}