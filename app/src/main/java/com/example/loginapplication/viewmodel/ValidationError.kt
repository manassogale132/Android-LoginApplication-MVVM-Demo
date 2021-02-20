package com.example.loginapplication.viewmodel

import androidx.annotation.StringRes
import com.example.loginapplication.R

enum class ValidationError(@StringRes val messageRes: Int) {
    EMPTY_EMAIL(R.string.validation_error_enter_email),
    INVALID_EMAIL(R.string.validation_error_invalid_email),
    EMPTY_PASSWORD(R.string.validation_error_enter_password),
    INVALID_PASSWORD(R.string.validation_error_invalid_password)
}
