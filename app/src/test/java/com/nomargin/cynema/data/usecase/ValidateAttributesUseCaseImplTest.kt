package com.nomargin.cynema.data.usecase

import com.google.common.truth.Truth.assertThat
import com.nomargin.cynema.R
import com.nomargin.cynema.data.local.PostModel
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.model.SignUpModel
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel
import org.junit.Before
import org.junit.Test

class ValidateAttributesUseCaseImplTest {

    private lateinit var validateAttributesUseCase: ValidateAttributesUseCase

    @Before
    fun setup() {
        validateAttributesUseCase = ValidateAttributesUseCaseImpl()
    }

    @Test
    fun `Empty email isn't valid`() {
        val signUpModel = SignUpModel("", "123456789", "123456789", true)
        val result = validateAttributesUseCase.validateSignUpAttributes(signUpModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.emailFieldIsEmpty,
                    R.string.email_field_is_empty
                )
            )
    }

    @Test
    fun `Empty password isn't valid`() {
        val signUpModel = SignUpModel("email@email.com", "", "123456789", true)
        val result = validateAttributesUseCase.validateSignUpAttributes(signUpModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.passwordFieldIsEmpty,
                    R.string.password_field_is_empty
                )
            )
    }

    @Test
    fun `Empty confirm password isn't valid`() {
        val signUpModel = SignUpModel("email@email.com", "123456789", "", true)
        val result = validateAttributesUseCase.validateSignUpAttributes(signUpModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.confirmPasswordFieldIsEmpty,
                    R.string.confirm_password_field_is_empty
                )
            )
    }

    @Test
    fun `Password less than 8 characters isn't valid`() {
        val signUpModel = SignUpModel("email@email.com", "1234567", "1234567", true)
        val result = validateAttributesUseCase.validateSignUpAttributes(signUpModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.passwordShouldHaveMoreThanEightCharacters,
                    R.string.password_should_have_more_than_8_char
                )
            )
    }

    @Test
    fun `Passwords don't match returns Resource-Error`() {
        val signUpModel = SignUpModel("email@email.com", "12345678", "1234567", true)
        val result = validateAttributesUseCase.validateSignUpAttributes(signUpModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.passwordsDoNotMatch,
                    R.string.password_do_not_match
                )
            )
    }

    @Test
    fun `User don't accepted the Terms of Use and Privacy Policy isn't valid`() {
        val signUpModel = SignUpModel("email@email.com", "12345678Aa", "12345678Aa", false)
        val result = validateAttributesUseCase.validateSignUpAttributes(signUpModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.theUserDidNotAcceptedTermsOfUseAndPrivacyPolicy,
                    R.string.terms_of_use_and_privacy_policy_did_not_accepted
                )
            )
    }

    @Test
    fun `All requirements are OK is valid`() {
        val signUpModel = SignUpModel("email@email.com", "12345678Aa", "12345678Aa", true)
        val result = validateAttributesUseCase.validateSignUpAttributes(signUpModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    isValid = true,
                    null,
                    R.string.sign_up_with_successfully
                )
            )
    }

    @Test
    fun `Empty user username returns Resource-Error`() {
        val userProfileModel = UserProfileModel(null, "name", "lastname", "", "")
        val result = validateAttributesUseCase.validateUserProfile(userProfileModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.usernameIsEmpty,
                    R.string.username_is_empty
                )
            )
    }

    @Test
    fun `Empty first user name returns Resource-Error`() {
        val userProfileModel = UserProfileModel(null, "", "lastname", "userusername", "")
        val result = validateAttributesUseCase.validateUserProfile(userProfileModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.firstNameIsEmpty,
                    R.string.first_name_is_empty
                )
            )
    }

    @Test
    fun `Empty first last name returns Resource-Error`() {
        val userProfileModel = UserProfileModel(null, "first", "", "userusername", "")
        val result = validateAttributesUseCase.validateUserProfile(userProfileModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.lastNameIsEmpty,
                    R.string.last_name_is_empty
                )
            )
    }

    @Test
    fun `Invalid user username returns Resource-Error`() {
        val userProfileModel = UserProfileModel(null, "first", "last", "userusername@@", "")
        val result = validateAttributesUseCase.validateUserProfile(userProfileModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.usernameIsNotValid,
                    R.string.valid_username_characters
                )
            )
    }

    @Test
    fun `Username bigger than allowed returns Resource-Error`() {
        var username = ""
        for (i in 0..Constants.MAX_LENGTH.userUsernameMaxLength + 10) {
            username += "a"
        }
        val userProfileModel = UserProfileModel(null, "first", "last", username, "")
        val result = validateAttributesUseCase.validateUserProfile(userProfileModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.usernameIsNotValid,
                    R.string.valid_username_characters
                )
            )
    }

    @Test
    fun `User biography bigger than allowed returns Resource-Error`() {
        var biography = ""
        for (i in 0..Constants.MAX_LENGTH.userBiographyMaxLength + 10) {
            biography += "a"
        }
        val userProfileModel = UserProfileModel(null, "first", "last", "userusername", biography)
        val result = validateAttributesUseCase.validateUserProfile(userProfileModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.userBiographyIsBiggerThanAllowed,
                    R.string.valid_user_biography
                )
            )
    }

    @Test
    fun `User profile required field OK returns Resource-Success`() {
        val userProfileModel = UserProfileModel(null, "first", "lastname", "userusername", "")
        val result = validateAttributesUseCase.validateUserProfile(userProfileModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    true,
                    null,
                    R.string.all_fields_are_checked
                )
            )
    }

    @Test
    fun `Discussion post title is empty returns Resource-Error`() {
        val postModel = PostModel(
            "",
            "lorem ipsum",
            false,
            "1000"
        )
        val result = validateAttributesUseCase.validatePost(postModel)
        assertThat(
            result
        ).isEqualTo(
            StatusModel(
                false,
                Constants.ERROR_TYPES.postTitleIsEmpty,
                R.string.post_title_is_empty
            )
        )
    }

    @Test
    fun `Discussion post body is empty returns Resource-Error`() {
        val postModel = PostModel(
            "lorem ipsum",
            "",
            false,
            "1000"
        )
        val result = validateAttributesUseCase.validatePost(postModel)
        assertThat(
            result
        ).isEqualTo(
            StatusModel(
                false,
                Constants.ERROR_TYPES.postBodyIsEmpty,
                R.string.post_body_is_empty
            )
        )
    }

    @Test
    fun `Discussion post title is bigger than allowed returns Resource-Error`() {
        var postTitle = ""
        for (i in 0..Constants.MAX_LENGTH.postTitleMaxLength + 10) {
            postTitle += "a"
        }
        val postModel = PostModel(
            postTitle,
            "",
            false,
            "1000"
        )
        val result = validateAttributesUseCase.validatePost(postModel)
        assertThat(
            result
        ).isEqualTo(
            StatusModel(
                false,
                Constants.ERROR_TYPES.postTitleIsBiggerThanAllowed,
                R.string.valid_post_title
            )
        )
    }

    @Test
    fun `Discussion post body is bigger than allowed empty returns Resource-Error`() {
        var postBody = ""
        for (i in 0..Constants.MAX_LENGTH.postBodyMaxLength + 10) {
            postBody += "a"
        }
        val postModel = PostModel(
            "lorem ipsum",
            postBody,
            false,
            "1000"
        )
        val result = validateAttributesUseCase.validatePost(postModel)
        assertThat(
            result
        ).isEqualTo(
            StatusModel(
                false,
                Constants.ERROR_TYPES.postBodyIsBiggerThanAllowed,
                R.string.valid_post_body
            )
        )
    }

    @Test
    fun `Discussion post title is lower than allowed returns Resource-Error`() {
        var postTitle = ""
        var postBody = ""
        for (i in 0 until Constants.MIN_LENGTH.postTitleMinLength - 1) {
            postTitle += "a"
        }
        for (i in 0..Constants.MIN_LENGTH.postBodyMinLength + 10) {
            postBody += "a"
        }
        val postModel = PostModel(
            postTitle,
            postBody,
            false,
            "1000"
        )
        val result = validateAttributesUseCase.validatePost(postModel)
        assertThat(
            result
        ).isEqualTo(
            StatusModel(
                false,
                Constants.ERROR_TYPES.postTitleIsLowerThanAllowed,
                R.string.valid_post_title
            )
        )
    }

    @Test
    fun `Discussion post body is lower than allowed returns Resource-Error`() {
        var postTitle = ""
        var postBody = ""
        for (i in 0..Constants.MIN_LENGTH.postTitleMinLength + 10) {
            postTitle += "a"
        }
        for (i in 0 until Constants.MIN_LENGTH.postBodyMinLength - 1) {
            postBody += "a"
        }
        val postModel = PostModel(
            postTitle,
            postBody,
            false,
            "1000"
        )
        val result = validateAttributesUseCase.validatePost(postModel)
        assertThat(
            result
        ).isEqualTo(
            StatusModel(
                false,
                Constants.ERROR_TYPES.postBodyIsLowerThanAllowed,
                R.string.valid_post_body
            )
        )
    }

    @Test
    fun `Discussion post all fields are OK returns Resource-Success`() {
        var postTitle = ""
        var postBody = ""
        for (i in 0..Constants.MIN_LENGTH.postTitleMinLength + 10) {
            postTitle += "a"
        }
        for (i in 0..Constants.MIN_LENGTH.postBodyMinLength + 10) {
            postBody += "a"
        }
        val postModel = PostModel(
            postTitle,
            postBody,
            false,
            "1000"
        )
        val result = validateAttributesUseCase.validatePost(postModel)
        assertThat(
            result
        ).isEqualTo(
            StatusModel(
                true,
                null,
                R.string.all_fields_are_checked
            )
        )
    }
}