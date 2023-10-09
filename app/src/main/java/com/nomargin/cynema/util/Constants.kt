package com.nomargin.cynema.util

import com.nomargin.cynema.R

object Constants {

    object ERROR_TYPES {
        const val emailFieldIsEmpty = 1
        const val passwordFieldIsEmpty = 2
        const val confirmPasswordFieldIsEmpty = 3
        const val passwordsDoNotMatch = 4
        const val passwordShouldHaveMoreThanEightCharacters = 5
        const val theUserDidNotAcceptedTermsOfUseAndPrivacyPolicy = 6
        const val firebaseAuthError = 7
        const val firebaseCredentialAuthError = 8
        const val apiException = 9
        const val firstNameIsEmpty = 10
        const val lastNameIsEmpty = 11
        const val usernameIsEmpty = 12
        const val usernameIsNotValid = 13
        const val userBiographyIsBiggerThanAllowed = 14
        const val passwordIsNotValid = 15
        const val userUsernameAlreadyInUse = 16
        const val couldNotReachTheUserData = 18
        const val postTitleIsEmpty = 19
        const val postTitleIsBiggerThanAllowed = 20
        const val postBodyIsEmpty = 21
        const val postBodyIsBiggerThanAllowed = 22
        const val postTitleIsLowerThanAllowed = 23
        const val postBodyIsLowerThanAllowed = 24
        const val couldNotReachTheDiscussionPost = 25
        const val userIsNotLoggedIn = 26
        const val commentBodyIsEmpty = 27
        const val commentBodyIsBiggerThanAllowed = 28
        const val commentBodyIsLowerThanAllowed = 29
        const val couldNotReachTheDiscussionPostComment = 30
    }

    object AUTH_ERRORS {
        val authErrors = mapOf(
            "ERROR_INVALID_CUSTOM_TOKEN" to R.string.error_login_custom_token,
            "ERROR_CUSTOM_TOKEN_MISMATCH" to R.string.error_login_custom_token_mismatch,
            "ERROR_INVALID_CREDENTIAL" to R.string.error_login_credential_malformed_or_expired,
            "ERROR_INVALID_EMAIL" to R.string.error_login_invalid_email,
            "ERROR_WRONG_PASSWORD" to R.string.error_login_wrong_password,
            "ERROR_USER_MISMATCH" to R.string.error_login_user_mismatch,
            "ERROR_REQUIRES_RECENT_LOGIN" to R.string.error_login_requires_recent_login,
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" to R.string.error_login_accounts_exits_with_different_credential,
            "ERROR_EMAIL_ALREADY_IN_USE" to R.string.error_login_email_already_in_use,
            "ERROR_CREDENTIAL_ALREADY_IN_USE" to R.string.error_login_credential_already_in_use,
            "ERROR_USER_DISABLED" to R.string.error_login_user_disabled,
            "ERROR_USER_TOKEN_EXPIRED" to R.string.error_login_user_token_expired,
            "ERROR_USER_NOT_FOUND" to R.string.error_login_user_not_found,
            "ERROR_INVALID_USER_TOKEN" to R.string.error_login_invalid_user_token,
            "ERROR_OPERATION_NOT_ALLOWED" to R.string.error_login_operation_not_allowed,
            "ERROR_WEAK_PASSWORD" to R.string.error_login_password_is_weak
        )
    }

    object MIN_LENGTH {
        const val passwordMinLength = 8
        const val usernameMinLength = 4
        const val postTitleMinLength = 4
        const val postBodyMinLength = 8
        const val commentBodyMinLength = 1
    }

    object MAX_LENGTH {
        const val userUsernameMaxLength = 24
        const val userBiographyMaxLength = 240
        const val postTitleMaxLength = 80
        const val postBodyMaxLength = 720
        const val commentBodyMaxLength = 254
    }

    object REGEX {
        const val passwordRegexPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,64}"
        const val usernameRegexPattern = "^([\\w]){4,24}\$"
    }

    object STORAGE {
        const val profilePictureRoot = "/profile_picture/"
    }

    object FIRESTORE {
        const val usersCollection = "users"
        const val postsCollection = "posts"
        const val commentsCollection = "comments"
        const val usersWhoVoted = "usersWhoVoted"
        const val usersWhoUpVoted = "usersWhoUpVoted"
        const val usersWhoDownVoted = "usersWhoDownVoted"
    }

    object TMDB_PATH_URLs {
        const val posterPathUrl = "https://image.tmdb.org/t/p/original/"
    }

    object LOCAL_STORAGE {
        const val databaseName = "application_local_database"
        const val databaseVersion = 1
        const val genreTableName = "genre"
        const val genreIdColumnName = "id"
        const val genreDescColumnName = "desc"
        const val sharedPreferencesGeneralKey = "movieIdGeneralKey"
        const val sharedPreferencesMovieIdKey = "movieId"
        const val sharedPreferencesPostIdKey = "postId"
    }

    enum class BUNDLE_KEYS {
        MovieDiscussionPostId,
        MovieDiscussionPostOwnerName
    }

    enum class UPDATE_TYPE {
        Upvote,
        Downvote
    }

    enum class VOTE_TYPE {
        Upvote,
        Downvote
    }

    object CLASS_TYPE {
        const val carouselModel = 1
        const val movieModel = 2
    }
}