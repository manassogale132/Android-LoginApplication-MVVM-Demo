package com.example.loginapplication.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserDataManager : UserDataManager {
    private val auth = FirebaseAuth.getInstance()

    override fun getCurrentUser(): User? = auth.currentUser?.toUser()

    override fun signInWithEmail(
        email: String,
        password: String,
        callback: (User?, Exception?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            val currentUser = it.user?.toUser()
            currentUser?.let {
                callback(currentUser, null)
                return@addOnSuccessListener
            }

            callback(null, IllegalStateException("User is found null on success"))
        }.addOnFailureListener {
            callback(null, it)
        }
    }
}

// extension function to map firebase user to our user model class
private fun FirebaseUser.toUser(): User = User(displayName, email)
