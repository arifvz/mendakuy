package com.arif.mendakuy.ui.register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.arif.mendakuy.databinding.ActivityRegisterBinding
import com.arif.mendakuy.data.model.User
import com.arif.mendakuy.ui.login.LoginActivity
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private var _binding: ActivityRegisterBinding? = null

    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerViewModel =
            ViewModelProvider(this).get(RegisterViewModel::class.java)

        _binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading..")

        onViewCreated()
        observeViewModel()
    }

    private fun observeViewModel() {
        registerViewModel.state.observe(this, { state ->
            when (state) {
                is RegisterViewState.Progress -> onProgress(state.isLoading)
                is RegisterViewState.RegisterResult -> onRegisterResult(state.userId, state.message)
            }
        })
    }

    private fun onRegisterResult(userId: String?, message: String?) {
        if (userId != null) {
            finish()
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun onProgress(loading: Boolean) {
        if (loading) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    private fun onViewCreated() {

        binding.apply {
            registerBtn.setOnClickListener {
                val user = User(
                    fullName = fullName.text.toString(),
                    phoneNumber = phoneNumber.text.toString(),
                    username = username.text.toString(),
                    password = password.text.toString(),
                    createdAt = Calendar.getInstance().time )
                registerViewModel.register(user)
                resultRegister()
            }

            btnBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    fun resultRegister(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}