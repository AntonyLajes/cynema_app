package com.nomargin.cynema_app.data.remote.entity

data class SignUpModel(
    var email: String = "",
    var password: String,
    var confirmPassword: String
)
