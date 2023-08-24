package com.nomargin.cynema_app.data.usecase

import com.nomargin.cynema_app.R
import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.util.Constants
import com.nomargin.cynema_app.util.StatusModel
import javax.inject.Inject

class ValidateAttributesUseCaseImpl @Inject constructor() : ValidateAttributesUseCase {

    override fun validateAttributes(signUpModel: SignUpModel): StatusModel {
        return when {
            signUpModel.email.isEmpty() -> {
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.emailFieldIsEmpty,
                    R.string.email_field_is_empty
                )
            }

            signUpModel.password.isEmpty() -> {
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.passwordFieldIsEmpty,
                    R.string.password_field_is_empty
                )
            }

            signUpModel.password.length < Constants.MIN_LENGTH.passwordMinLength -> {
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.passwordShouldHaveMoreThanEightCharacters,
                    R.string.password_should_have_more_than_8_char
                )
            }

            signUpModel.confirmPassword.isEmpty() -> {
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.confirmPasswordFieldIsEmpty,
                    R.string.confirm_password_field_is_empty
                )
            }

            signUpModel.password != signUpModel.confirmPassword -> {
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.passwordDoNotMatch,
                    R.string.password_do_not_match
                )
            }

            else -> {
                StatusModel(
                    isValid = true,
                    null,
                    R.string.sign_up_with_successfully
                )
            }
        }
    }

}