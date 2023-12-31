package com.nomargin.cynema.data.usecase

import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.entity.CommentModel
import com.nomargin.cynema.data.local.entity.PostModel
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.model.SignInModel
import com.nomargin.cynema.util.model.SignUpModel
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel
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

            !Regex(Constants.REGEX.passwordRegexPattern).containsMatchIn(signUpModel.password) -> {
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.passwordIsNotValid,
                    R.string.password_is_not_valid
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

    override fun validateUserProfile(userProfileModel: UserProfileModel): StatusModel {
        return when {
            userProfileModel.userFirstName.isEmpty() -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.firstNameIsEmpty,
                    R.string.first_name_is_empty
                )
            }

            userProfileModel.userLastName.isEmpty() -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.lastNameIsEmpty,
                    R.string.last_name_is_empty
                )
            }

            userProfileModel.userUsername.isEmpty() -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.usernameIsEmpty,
                    R.string.username_is_empty
                )
            }

            !Regex(Constants.REGEX.usernameRegexPattern).matches(userProfileModel.userUsername) -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.usernameIsNotValid,
                    R.string.valid_username_characters
                )
            }

            userProfileModel.userBiography.length > Constants.MAX_LENGTH.userBiographyMaxLength -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.userBiographyIsBiggerThanAllowed,
                    R.string.valid_user_biography
                )
            }

            else -> {
                StatusModel(
                    true,
                    null,
                    R.string.all_fields_are_checked
                )
            }
        }
    }

    override fun validatePost(postModel: PostModel): StatusModel {
        return when {
            postModel.title.isEmpty() -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.postTitleIsEmpty,
                    R.string.post_title_is_empty
                )
            }

            postModel.title.length > Constants.MAX_LENGTH.postTitleMaxLength -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.postTitleIsBiggerThanAllowed,
                    R.string.valid_post_title
                )
            }

            postModel.body.isEmpty() -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.postBodyIsEmpty,
                    R.string.post_body_is_empty
                )
            }

            postModel.body.length > Constants.MAX_LENGTH.postBodyMaxLength -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.postBodyIsBiggerThanAllowed,
                    R.string.valid_post_body
                )
            }

            postModel.title.length < Constants.MIN_LENGTH.postTitleMinLength -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.postTitleIsLowerThanAllowed,
                    R.string.valid_post_title
                )
            }

            postModel.body.length < Constants.MIN_LENGTH.postBodyMinLength -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.postBodyIsLowerThanAllowed,
                    R.string.valid_post_body
                )
            }

            else -> {
                StatusModel(
                    true,
                    null,
                    R.string.all_fields_are_checked
                )
            }
        }
    }

    override fun validateComment(commentModel: CommentModel): StatusModel {
        return when {
            commentModel.body.isEmpty() -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.commentBodyIsEmpty,
                    R.string.comment_body_is_empty
                )
            }

            commentModel.body.length > Constants.MAX_LENGTH.commentBodyMaxLength -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.commentBodyIsBiggerThanAllowed,
                    R.string.valid_comment_body
                )
            }

            commentModel.body.length < Constants.MIN_LENGTH.commentBodyMinLength -> {
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.commentBodyIsLowerThanAllowed,
                    R.string.valid_comment_body
                )
            }

            else -> {
                StatusModel(
                    true,
                    null,
                    R.string.all_fields_are_checked
                )
            }
        }
    }
}