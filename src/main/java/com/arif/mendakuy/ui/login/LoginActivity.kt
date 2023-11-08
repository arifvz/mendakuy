package com.arif.mendakuy.ui.login
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.arif.mendakuy.data.AuthManager
import com.arif.mendakuy.data.model.User
import com.arif.mendakuy.databinding.ActivityLoginBinding
import com.arif.mendakuy.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        loginViewModel =
            ViewModelProvider(this)[LoginViewModel::class.java]

        _binding = ActivityLoginBinding.inflate(LayoutInflater.from(this),
            null, false)
        setContentView(binding.root)
        progressBar = ProgressBar(this)
        setTitle("PLEASE WAIT")
        onViewCreated()
        observeViewModel()
    }

    private fun observeViewModel() {
        loginViewModel.state.observe(this) { state ->
            when (state) {
                is LoginViewState.Progress -> onProgress(state.isLoading)
                is LoginViewState.LoginResult -> onLoginResult(
                    state.user,
                    state.message
                )
            }
        }
    }

    private fun onLoginResult(user: User?, message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        if (user != null) {
            AuthManager.setUser(this, user)
            finish()
        }
    }

    private fun onProgress(loading: Boolean) {
        if (loading) {
            progressBar.isVisible
        } else {
            progressBar.isInvisible
        }
    }

    private fun onViewCreated() {
        binding.loginBtn.setOnClickListener {
            loginViewModel.onLogin(
                binding.username.text.toString(),
                binding.password.text.toString())
            val imm = getSystemService(INPUT_METHOD_SERVICE) as
                    InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }

        binding.signupBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent) }
    }
}