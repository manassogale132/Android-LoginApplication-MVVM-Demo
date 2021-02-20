package com.example.loginapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.loginapplication.R
import com.example.loginapplication.model.FirebaseUserDataManager
import com.example.loginapplication.model.User
import com.example.loginapplication.viewmodel.LoginViewModel
import com.example.loginapplication.viewmodel.UserState
import com.example.loginapplication.viewmodel.ValidationError
import com.example.loginapplication.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initGlobals()
        checkCurrentAuthState()
        observeValidationErrors()
        setClickOnRegisterButton()
        setClickOnLoginButton()
    }

    private fun initGlobals() {
        val userDataManager = FirebaseUserDataManager()
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(userDataManager)
        ).get(LoginViewModel::class.java)
    }

    private fun observeValidationErrors() {
        viewModel.validationError.observe(this, Observer { validationError ->
            if (validationError == null) return@Observer

            when (validationError) {
                ValidationError.EMPTY_EMAIL -> txtEmail.error = getString(validationError.messageRes)

                ValidationError.INVALID_EMAIL -> {
                    txtEmail.error = getString(validationError.messageRes)
                    txtEmail.requestFocus()
                }

                ValidationError.EMPTY_PASSWORD -> {
                    txtPwd.error = getString(validationError.messageRes)
                    txtPwd.requestFocus()
                }

                ValidationError.INVALID_PASSWORD -> {
                    txtPwd.error = getString(validationError.messageRes)
                }
            }
        })
    }

    private fun setClickOnLoginButton() {
        btnLogin.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPwd.text.toString()
            viewModel.signInWithEmail(email, password)
        }
    }

    private fun setClickOnRegisterButton() {
        loginToRegister.setOnClickListener {
            Toast.makeText(this, "Register Page Opened!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkCurrentAuthState() {
        viewModel.currentUserState.observe(this, Observer { userState ->
            // this block will execute whenever there is a new value posted in live-data

            when (userState) {
                UserState.Loading -> {
                    // can show progress bar to show loading state
                }

                is UserState.Success -> {
                    // can dismiss progress bar to dismiss loading state
                    updateUI(userState.currentUser)
                }

                is UserState.Failure -> {
                    // can dismiss progress bar to dismiss loading state
                    userState.exception.printStackTrace()

                    // can check for custom exception for invalid email or password
                    // and can show appropriate message
                    Toast.makeText(baseContext, "Wrong email or password.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun updateUI(currentUser: User) {
        // can use the currentUser parameter to pass to another activity
        Toast.makeText(baseContext, "Login Successful!", Toast.LENGTH_SHORT).show()
        startActivity(
            Intent(
                this,
                DashboardActivity::class.java
            )
        )
        finish()
    }
}