package com.example.loginapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginapplication.model.UserDataManager

class ViewModelFactory(
    private val userDataManager: UserDataManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(userDataManager) as T

        throw IllegalStateException("Can not instantiate view-model")
    }
}