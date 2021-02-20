package com.example.loginapplication.model

interface UserDataManager {
    fun getCurrentUser(): User?
    fun signInWithEmail(email: String, password: String, callback: (User?, Exception?) -> Unit)

}