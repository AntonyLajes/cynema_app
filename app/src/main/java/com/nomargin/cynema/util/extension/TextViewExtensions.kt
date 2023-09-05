package com.nomargin.cynema.util.extension

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

object TextViewExtensions {

    fun TextView.changeTextColor(@ColorRes color: Int) {
        setTextColor(
            ContextCompat.getColor(
                context,
                color
            )
        )
    }

    fun TextView.changeDrawableColor(@ColorRes color: Int) {
        compoundDrawables.filterNotNull()
            .forEach { drawable ->
                drawable.mutate()
                drawable.setTint(
                    ContextCompat.getColor(
                        context,
                        color
                    )
                )
            }
    }

    fun TextView.changeStartDrawable(@DrawableRes drawable: Int) {
        val imgDrawable = AppCompatResources.getDrawable(context, drawable)
        setCompoundDrawablesWithIntrinsicBounds(imgDrawable, null, null, null)
    }
}