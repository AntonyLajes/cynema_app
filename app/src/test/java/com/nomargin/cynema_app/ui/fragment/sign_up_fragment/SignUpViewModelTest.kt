package com.nomargin.cynema_app.ui.fragment.sign_up_fragment

import com.google.common.truth.Truth.assertThat
import com.nomargin.cynema_app.R
import com.nomargin.cynema_app.data.remote.entity.SignUpModel
import com.nomargin.cynema_app.data.repository.FakeAuthenticationRepository
import com.nomargin.cynema_app.util.Constants
import com.nomargin.cynema_app.util.Resource
import com.nomargin.cynema_app.util.StatusModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {

    private lateinit var fakeAuthenticationRepository: FakeAuthenticationRepository

    @Before
    fun setup() {
        fakeAuthenticationRepository = FakeAuthenticationRepository()
    }

    @Test
    fun `All SignUp parameters passed corrected return true`() = runTest {
        val result = fakeAuthenticationRepository.signUp(
            SignUpModel(
                "email@email.com",
                "123456789",
                "123456789"
            )
        )
        assertThat(result).isEqualTo(
            Resource.success(
                StatusModel(
                    false,
                    null,
                    R.string.sign_up_with_successfully
                ),
                null
            )
        )
    }

    @Test
    fun `Empty email field should returns false`() = runTest {
        fakeAuthenticationRepository.shouldHappenAnError(
            true,
            Constants.ERROR_TYPES.emailFieldIsEmpty,
            R.string.email_field_is_empty
        )
        val result = fakeAuthenticationRepository.signUp(
            SignUpModel(
                "",
                "123456789",
                "123456789"
            )
        )
        assertThat(result).isEqualTo(
            Resource.error(
                "Error",
                StatusModel(
                    false,
                    Constants.ERROR_TYPES.emailFieldIsEmpty,
                    R.string.email_field_is_empty
                ),
                null
            )
        )
    }

}