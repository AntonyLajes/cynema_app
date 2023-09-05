package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.entity.SignInModel
import com.nomargin.cynema.data.remote.entity.SignUpModel
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.model.StatusModel
import javax.inject.Inject

class ValidateAttributesUseCaseImpl @Inject constructor() : ValidateAttributesUseCase {

    override fun validateSignUpAttributes(signUpModel: SignUpModel): StatusModel {
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
                    Constants.ERROR_TYPES.passwordsDoNotMatch,
                    R.string.password_do_not_match
                )
            }

            !signUpModel.acceptedTermsAndPrivacyPolicy -> {
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.theUserDidNotAcceptedTermsOfUseAndPrivacyPolicy,
                    R.string.terms_of_use_and_privacy_policy_did_not_accepted
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

    override fun validateSignInAttributes(signInModel: SignInModel): StatusModel {
        return when {
            signInModel.email.isEmpty() -> {
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.emailFieldIsEmpty,
                    R.string.email_field_is_empty
                )
            }

            signInModel.password.isEmpty() -> {
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.passwordFieldIsEmpty,
                    R.string.password_field_is_empty
                )
            }

            else -> {
                StatusModel(
                    isValid = true,
                    null,
                    R.string.all_fields_are_checked
                )
            }
        }
    }

    override fun validateSendPasswordResetEmailAttributes(email: String): StatusModel {
        return if (email.isEmpty()) {
            StatusModel(
                false,
                Constants.ERROR_TYPES.emailFieldIsEmpty,
                R.string.email_field_is_empty
            )
        } else {
            StatusModel(
                true,
                null,
                R.string.sent_password_reset_email_with_success
            )
        }
    }
}