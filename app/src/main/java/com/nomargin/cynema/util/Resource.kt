package com.nomargin.cynema.util

import com.nomargin.cynema.util.model.StatusModel

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val statusModel: StatusModel?) {
    companion object {
        fun <T> success(data: T?, statusModel: StatusModel?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, statusModel)
        }

        fun <T> error(msg: String, data: T?, statusModel: StatusModel?): Resource<T> {
            return Resource(Status.ERROR, data, msg, statusModel)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}