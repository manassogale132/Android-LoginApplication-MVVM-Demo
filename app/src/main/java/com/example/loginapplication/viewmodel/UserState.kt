package com.example.loginapplication.viewmodel

import com.example.loginapplication.model.User

sealed class UserState {
    object Loading : UserState()
    class Success(val currentUser : User) : UserState()
    class Failure(val exception : Exception) : UserState()
}
