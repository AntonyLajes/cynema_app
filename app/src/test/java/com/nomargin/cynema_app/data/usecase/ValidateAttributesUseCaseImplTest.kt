package com.nomargin.cynema_app.data.usecase

import com.google.common.truth.Truth.assertThat
import com.nomargin.cynema_app.R
import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.util.Constants
import com.nomargin.cynema_app.util.StatusModel
import org.junit.Before
import org.junit.Test

class ValidateAttributesUseCaseImplTest {

    private lateinit var validAttributesUseCase: ValidateAttributesUseCase

    @Before
    fun setup() {
        validAttributesUseCase = ValidateAttributesUseCaseImpl()
    }

    @Test
    fun `Empty email isn't valid`() {
        val signUpModel = SignUpModel("", "123456789", "123456789", true)
        val result = validAttributesUseCase.validateAttributes(signUpModel)
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
        val result = validAttributesUseCase.validateAttributes(signUpModel)
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
        val result = validAttributesUseCase.validateAttributes(signUpModel)
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
        val result = validAttributesUseCase.validateAttributes(signUpModel)
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
        val result = validAttributesUseCase.validateAttributes(signUpModel)
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
        val signUpModel = SignUpModel("email@email.com", "12345678", "12345678", false)
        val result = validAttributesUseCase.validateAttributes(signUpModel)
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
        val signUpModel = SignUpModel("email@email.com", "12345678", "12345678", false)
        val result = validAttributesUseCase.validateAttributes(signUpModel)
        assertThat(result)
            .isEqualTo(
                StatusModel(
                    isValid = false,
                    Constants.ERROR_TYPES.theUserDidNotAcceptedTermsOfUseAndPrivacyPolicy,
                    R.string.terms_of_use_and_privacy_policy_did_not_accepted
                )
            )
    }

}