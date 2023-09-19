package com.nomargin.cynema.util.extension

interface AdapterOnItemClickListener {

    fun <T> onItemClickListener(
        item: T,
        position: Int
    )

}