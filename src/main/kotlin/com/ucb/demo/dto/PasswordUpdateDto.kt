package com.ucb.demo.dto

data class PasswordUpdateDto (
        val currentPassword: String,
        val newPassword: String,
        val confirmNewPassword: String
)
