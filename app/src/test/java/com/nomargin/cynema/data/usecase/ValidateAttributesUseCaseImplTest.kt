package com.nomargin.cynema.data.usecase

import com.google.common.truth.Truth.assertThat
import com.nomargin.cynema.R
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
        for(i in 0..Constants.MAX_LENGTH.userUsernameMaxLength + 10){
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
        for(i in 0..Constants.MAX_LENGTH.userBiographyMaxLength + 10){
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
    fun `Required field OK returns Resource-Success`() {
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
}