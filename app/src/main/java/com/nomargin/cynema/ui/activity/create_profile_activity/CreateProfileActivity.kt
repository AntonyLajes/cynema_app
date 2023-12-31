package com.nomargin.cynema.ui.activity.create_profile_activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nomargin.cynema.R
import com.nomargin.cynema.data.remote.firebase.entity.UserProfileDataModel
import com.nomargin.cynema.databinding.ActivityCreateProfileBinding
import com.nomargin.cynema.ui.activity.main_activity.MainActivity
import com.nomargin.cynema.util.Constants
import com.nomargin.cynema.util.FrequencyFunctions
import com.nomargin.cynema.util.extension.TextInputLayoutExtensions.setFieldError
import com.nomargin.cynema.util.model.StatusModel
import com.nomargin.cynema.util.model.UserProfileModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateProfileActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private var galleryPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private val binding: ActivityCreateProfileBinding by lazy {
        ActivityCreateProfileBinding.inflate(
            layoutInflater
        )
    }
    private val firstNameInput: TextInputEditText by lazy { binding.firstNameInput }
    private val lastNameInput: TextInputEditText by lazy { binding.lastNameInput }
    private val usernameInput: TextInputEditText by lazy { binding.usernameInput }
    private val userBiography: TextInputEditText by lazy { binding.bioInput }
    private val userBiographyInputLayout: TextInputLayout by lazy { binding.bioInputLayout }
    private val firstNameInputLayout: TextInputLayout by lazy { binding.firstNameInputLayout }
    private val lastNameInputLayout: TextInputLayout by lazy { binding.lastNameInputLayout }
    private val usernameInputLayout: TextInputLayout by lazy { binding.usernameInputLayout }
    private val createProfileViewModel: CreateProfileViewModel by viewModels()
    private lateinit var alertDialog: AlertDialog
    private var mode: String? = null
    private var imageUri: Uri? = null
    private var isLoaded: Boolean = false
    private val galleryRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            if (permission) {
                imagePickerResult.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            } else {
                showDialogPermissionRequest()
            }
        }
    private val imagePickerResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                imageUri = result.data!!.data!!
                binding.profileImage.setImageURI(imageUri)
            } catch (e: Exception) {
                Log.d("imageUri", "imageUri: $e")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        verifyMode()
    }

    override fun onStart() {
        super.onStart()
        initClicks()
        focusWatcher()
        inputWatcher()
        observers()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.profileImage.id -> {
                verifyGalleryPermission()
            }

            binding.buttonFinishProfile.id -> {
                createProfileViewModel.saveProfile(
                    UserProfileModel(
                        imageUri,
                        firstNameInput.text.toString(),
                        lastNameInput.text.toString(),
                        usernameInput.text.toString(),
                        binding.bioInput.text.toString()
                    )
                )
            }
        }
    }

    private fun verifyMode() {
        val extras = intent.extras
        extras?.let {
            mode = it.getString(Constants.BUNDLE_KEYS.UserProfileHandlerType.name)
            isLoaded = true
            if (mode == Constants.UPDATE_TYPE.UpdateProfile.name) {
                if (Build.VERSION.SDK_INT >= 33) {
                    updateProfileFieldsHandler(
                        it.getParcelable(
                            Constants.BUNDLE_KEYS.UserData.name,
                            UserProfileDataModel::class.java
                        )
                    )
                } else {
                    updateProfileFieldsHandler(it.getParcelable(Constants.BUNDLE_KEYS.UserData.name))
                }
            }
        }
    }

    private fun updateProfileFieldsHandler(userData: UserProfileDataModel?) {
        binding.textScreenTitle.text = getString(R.string.profile)
        binding.textScreenSubtitle.text = getString(R.string.edit_your_profile)
        binding.buttonFinishProfile.text = getString(R.string.save)
        userData?.let {
            binding.firstNameInput.setText(it.firstName)
            binding.lastNameInput.setText(it.lastName)
            binding.usernameInput.setText(it.username)
            binding.bioInput.setText(it.biography)
            if(it.profilePicture?.isNotEmpty() == true){
                Glide.with(this)
                    .load(it.profilePicture)
                    .error(R.drawable.pic_profile_picture)
                    .into(binding.profileImage)
            }
        }
    }

    private fun observers() {
        createProfileViewModel.createProfileStatus.observe(this) { createProfileStatus ->
            createProfileStatus?.let {
                if (createProfileStatus.isValid) {
                    FrequencyFunctions.makeToast(this, createProfileStatus.message)
                    FrequencyFunctions.startNewActivityFromCurrentActivity(this, MainActivity())
                } else {
                    FrequencyFunctions.makeToast(this, createProfileStatus.message)
                }
                errorHandler(createProfileStatus)
            }
        }
        createProfileViewModel.userUsernameStatus.observe(this) { userUsernameStatus ->
            userUsernameStatus?.let {
                if (it.isValid) {
                    usernameInputLayout.boxStrokeColor = getColor(R.color.success_green)
                    usernameInputLayout.setFieldError(null)
                } else {
                    usernameInputLayout.setFieldError(it.message)
                }
            }
        }
    }

    private fun errorHandler(value: StatusModel) {
        if (!value.isValid) {
            when (value.errorType) {
                Constants.ERROR_TYPES.firstNameIsEmpty -> {
                    firstNameInputLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.lastNameIsEmpty -> {
                    lastNameInputLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.usernameIsEmpty -> {
                    usernameInputLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.usernameIsNotValid -> {
                    usernameInputLayout.setFieldError(value.message)
                }

                Constants.ERROR_TYPES.userBiographyIsBiggerThanAllowed -> {
                    userBiographyInputLayout.setFieldError(value.message)
                }
            }
        }
    }

    private fun inputWatcher() {
        firstNameInput.doAfterTextChanged { text ->
            if (text.toString().isNotEmpty()) {
                firstNameInputLayout.setFieldError(null)
            }
        }
        lastNameInput.doAfterTextChanged { text ->
            if (text.toString().isNotEmpty()) {
                lastNameInputLayout.setFieldError(null)
            }
        }
        usernameInput.doAfterTextChanged { text ->
            usernameInputLayout.isCounterEnabled = if (text.toString().isNotEmpty()) {
                usernameInputLayout.setFieldError(null)
                createProfileViewModel.checkUserUsername(text.toString())
                true
            } else {
                usernameInputLayout.boxStrokeColor = getColor(R.color.custom_bold_grey)
                false
            }
        }
        userBiography.doAfterTextChanged { text ->
            userBiographyInputLayout.isCounterEnabled = text.toString().isNotEmpty()
        }
    }

    private fun focusWatcher() {
        usernameInputLayout.isCounterEnabled = usernameInputLayout.hasFocus()
        userBiographyInputLayout.isCounterEnabled = usernameInputLayout.hasFocus()
    }

    private fun initClicks() {
        binding.buttonFinishProfile.setOnClickListener(this)
        binding.profileImage.setOnClickListener(this)
    }

    private fun verifyPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            galleryPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun verifyGalleryPermission() {
        val permissionAccepted = verifyPermission()

        when {
            permissionAccepted -> {
                imagePickerResult.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            }

            shouldShowRequestPermissionRationale(galleryPermission) -> {
                showDialogPermissionRequest()
            }

            else -> {
                galleryRequest.launch(
                    galleryPermission
                )
            }
        }

    }

    private fun showDialogPermissionRequest() {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.attention)
            .setMessage(R.string.permission_attention_advise)
            .setPositiveButton(R.string.yes) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                alertDialog.dismiss()
            }
            .setNegativeButton(R.string.no) { _, _ ->
                alertDialog.dismiss()
            }
        alertDialog = builder.create()
        alertDialog.show()
    }

}