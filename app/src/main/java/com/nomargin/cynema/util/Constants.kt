package com.nomargin.cynema.util

object Constants {

    object ERROR_TYPES{
        const val emailFieldIsEmpty = 1
        const val passwordFieldIsEmpty = 2
        const val confirmPasswordFieldIsEmpty = 3
        const val passwordsDoNotMatch = 4
        const val passwordShouldHaveMoreThanEightCharacters = 5
        const val theUserDidNotAcceptedTermsOfUseAndPrivacyPolicy = 6
        const val firebaseSignUpError = 7
    }

    object MIN_LENGTH{
        const val passwordMinLength = 8
    }

    object FIELD_HANDLER{
        const val isSuccess = true
    }

    object REGEX {
        const val passwordRegexPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,64}"
    }

}