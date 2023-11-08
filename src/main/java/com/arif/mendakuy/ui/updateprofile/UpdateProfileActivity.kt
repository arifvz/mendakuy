package com.arif.mendakuy.ui.updateprofile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.arif.mendakuy.App.Companion.getAvatarUrl
import com.arif.mendakuy.R
import com.arif.mendakuy.databinding.ActivityUpdateProfileBinding
import com.arif.mendakuy.ui.profile.UpdateProfileViewModel
import com.arif.mendakuy.ui.profile.UpdateProfileViewState
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import com.arif.mendakuy.data.model.User as user

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var updateProfileViewModel: UpdateProfileViewModel
    private lateinit var progressBar: ProgressBar

    private var _binding: ActivityUpdateProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateProfileViewModel =
            ViewModelProvider(this).get(UpdateProfileViewModel::class.java)

        _binding = ActivityUpdateProfileBinding.inflate(LayoutInflater.from(this),null, false)
        setContentView(binding.root)

        onViewCreated()
    }

    private fun onViewCreated() {
        Firebase.auth.signInAnonymously()

        binding.apply {

            updateProfileViewModel.getProfile()

            updateProfileViewModel.user.observe(this@UpdateProfileActivity){ user ->
                binding?.apply {
                    etUsername.setText(user?.username)
                    etFullName.setText(user?.fullName)
                    etPassword.setText(user?.password)
                    etPhoneNumber.setText(user?.phoneNumber)

                    user?.username?.getAvatarUrl {
                        Glide.with(this@UpdateProfileActivity)
                            .load(it)
                            .placeholder(R.drawable.ic_default_avatar)
                            .into(binding.ivProfilePic)
                    }
                }
            }

            updateProfileViewModel.state.observe(this@UpdateProfileActivity) { state ->
                when (state) {
                    is UpdateProfileViewState.Progress -> onProgress(state.isLoading)
                    is UpdateProfileViewState.UpdateProfile -> onSuccessUpdateProfile()
                    else -> {

                    }
                }
            }


                binding.fab.setOnClickListener {
                ImagePicker.create(this@UpdateProfileActivity)
                    .single()
                    .start()
            }

            btnUpdateProfile.setOnClickListener {

                val user = user(
                    username = etUsername.text.toString(),
                    fullName = etFullName.text.toString(),
                    phoneNumber = etPhoneNumber.text.toString(),
                    password = etPassword.text.toString()
                )
                updateProfileViewModel.onUpdateProfile(user)

//                intent.putExtra("username", tvUsername.toString())
//                intent.putExtra("fullName", tvFullname.toString())
//                intent.putExtra("phoneNumber", tvPhoneNumber.toString())
//                intent.putExtra("password",tvPassword.toString())
            }

            binding.btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun onProgress(loading: Boolean) {
        if (loading) {
            progressBar.isVisible
        } else {
            progressBar.isInvisible
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getFirstImageOrNull(data)

            Glide.with(this)
                .load(image.path)
                .into(binding.ivProfilePic)

            Handler().postDelayed({
                try {
                    binding.ivProfilePic.isDrawingCacheEnabled = true
                    binding.ivProfilePic.buildDrawingCache()
                    val bitmap = (binding.ivProfilePic.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                    val byte = baos.toByteArray()

                    updateProfileViewModel.uploadProfilePict(byte)
                }catch (e :Exception){
                    e.printStackTrace()
                }
            }, 1500)
        }
    }

    private fun onSuccessUpdateProfile() {
        finish()
    }

}

