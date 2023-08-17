package com.nomargin.cynema_app.util

object Constants {

    object ERROR_TYPES{
        const val emailFieldIsEmpty = 1
        const val passwordFieldIsEmpty = 2
        const val confirmPasswordFieldIsEmpty = 3
        const val passwordDoNotMatch = 4
        const val passwordShouldHaveMoreThanEightCharacters = 5
    }

    object MIN_LENGTH{
        const val passwordMinLength = 8
    }

}