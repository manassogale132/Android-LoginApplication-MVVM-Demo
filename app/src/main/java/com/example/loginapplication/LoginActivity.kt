package com.example.loginapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val currentUser:FirebaseUser? = auth.currentUser
        updateUI(currentUser)

        loginToRegister.setOnClickListener {
            Toast.makeText(this, "Register Page Opened!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this , RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            loginUserValidation()
        }
    }

    private fun loginUserValidation() {

        if (txtEmail.text.toString().isEmpty()) {
            txtEmail.error = "Please enter Email"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.text.toString()).matches()) {
            txtEmail.error = "Please enter Valid Email"
            txtEmail.requestFocus()
            return
        }
        if (txtPwd.text.toString().isEmpty()) {
            txtPwd.error = "Please enter Password"
            txtPwd.requestFocus()
            return
        }
        if (txtPwd.length() < 6) {
            txtPwd.error = "Password must be > 6 characters"
            return
        }

        auth.signInWithEmailAndPassword(txtEmail.text.toString(), txtPwd.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Wrong email or password.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {                //if user is already logged in then we get user details in 'currentUser' otherwise 'null'
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        // val currentUser:FirebaseUser? = auth.currentUser
        // updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            Toast.makeText(baseContext, "Login Successful!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,
                DashboardActivity::class.java))
            finish()
        }
    }
}