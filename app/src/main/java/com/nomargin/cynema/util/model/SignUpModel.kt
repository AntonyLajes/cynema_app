package com.nomargin.cynema.util.model

data class SignUpModel(
    var email: String = "",
    var password: String,
    var confirmPassword: String,
    var acceptedTermsAndPrivacyPolicy: Boolean = false
)
