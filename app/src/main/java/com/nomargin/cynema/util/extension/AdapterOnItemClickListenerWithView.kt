package com.nomargin.cynema.util.extension

import android.view.View

interface AdapterOnItemClickListenerWithView {

    fun <T> onItemClickListener(
        view: View,
        item: T,
        position: Int
    )

}