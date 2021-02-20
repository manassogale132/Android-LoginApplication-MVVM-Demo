package com.example.loginapplication.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loginapplication.model.User
import com.example.loginapplication.model.UserDataManager

class LoginViewModel(
    private val userDataManager: UserDataManager
) : ViewModel() {
    // this is backing property i.e _currentUser live-data can be changed from this class only
    private val _currentUserState = MutableLiveData<UserState>()
    val currentUserState: LiveData<UserState> = _currentUserState

    private val _validationError = MutableLiveData<ValidationError>()
    val validationError : LiveData<ValidationError> = _validationError

    init {
        val currentUser = userDataManager.getCurrentUser()
        currentUser?.let { _currentUserState.value = UserState.Success(currentUser) }
    }

    fun signInWithEmail(email: String, password: String) {
        if (!areValidFields(email, password)) return

        _currentUserState.value = UserState.Loading
        userDataManager.signInWithEmail(email, password) { user: User?, exception: Exception? ->
            exception?.let {
                _currentUserState.value = UserState.Failure(it)
            }
            user?.let {
                _currentUserState.value = UserState.Success(it)
            }
        }
    }

    private fun areValidFields(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            _validationError.value = ValidationError.EMPTY_EMAIL
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _validationError.value = ValidationError.INVALID_EMAIL
            return false
        }

        if (password.isEmpty()) {
            _validationError.value = ValidationError.EMPTY_PASSWORD
            return false
        }
        if (password.length < 6) {
            _validationError.value = ValidationError.INVALID_PASSWORD
            return false
        }

        _validationError.value = null
        return true
    }
}
