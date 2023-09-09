package com.nomargin.cynema.ui.activity.create_profile_activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.nomargin.cynema.R
import com.nomargin.cynema.databinding.ActivityCreateProfileBinding
import com.nomargin.cynema.util.model.UserProfileModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.security.Permission

@AndroidEntryPoint
class CreateProfileActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private val binding: ActivityCreateProfileBinding by lazy {
        ActivityCreateProfileBinding.inflate(
            layoutInflater
        )
    }
    private val createProfileViewModel: CreateProfileViewModel by viewModels()
    private lateinit var alertDialog: AlertDialog
    private lateinit var imageBitMap: Bitmap
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
                imageBitMap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        baseContext.contentResolver,
                        result.data?.data
                    )
                } else {
                    val source = ImageDecoder.createSource(
                        this.contentResolver,
                        result.data?.data!!
                    )
                    ImageDecoder.decodeBitmap(source)
                }

                binding.profileImage.setImageBitmap(imageBitMap)
            }catch (e: Exception){
                Log.d("imageBitMap", "imageBitMap: $e")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initClicks()
    }

    override fun onClick(view: View) {
        when(view.id){
            binding.profileImage.id -> {
                verifyGalleryPermission()
            }
            binding.buttonFinishProfile.id -> {
                createProfileViewModel.saveProfile(
                    UserProfileModel(
                        imageBitMap,
                        binding.firstNameInput.text.toString(),
                        binding.lastNameInput.text.toString(),
                        binding.usernameInput.text.toString(),
                        binding.bioInput.text.toString()
                    )
                )
            }
        }
    }

    private fun initClicks(){
        binding.buttonFinishProfile.setOnClickListener(this)
        binding.profileImage.setOnClickListener(this)
    }

    private fun verifyPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun verifyGalleryPermission() {
        val permissionAccepted = verifyPermission(galleryPermission)

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