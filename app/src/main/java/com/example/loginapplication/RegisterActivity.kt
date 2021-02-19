package com.example.loginapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            signUpUserValidation()
        }
    }

    private fun signUpUserValidation() {               //validation and signup method

        if (txtFullName.text.toString().isEmpty()) {
            txtFullName.error = "Please enter Fullname"
            txtFullName.requestFocus()
            return
        }

        if (txtFullName.text.toString().matches("[0-9*$%#&^()@!_+{}';]*".toRegex())) {
            txtFullName.error = "Please enter proper Fullname"
            txtFullName.requestFocus()
            return
        }

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
            txtPwd.setError("Password must be > 6 characters")
            return
        }

        auth.createUserWithEmailAndPassword(txtEmail.text.toString(), txtPwd.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val db = FirebaseFirestore.getInstance()
                    val fullname = txtFullName.editableText.toString()
                    val email = txtEmail.editableText.toString()

                    val user: MutableMap<String, Any> = HashMap()
                    user["Full Name"] = fullname
                    user["Email Id"] = email

                    db.collection("User Profiles")
                        .add(user).addOnSuccessListener {
                            Log.d("TAG", "DocumentSnapshot added with ID: ${it.id}")
                        }
                        .addOnFailureListener {
                            Log.w("TAG", "Error adding document", it)
                        }

                    Toast.makeText(baseContext, "User registered successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed.Try again", Toast.LENGTH_SHORT).show()
                }
            }
    }
}