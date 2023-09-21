package com.ucb.demo.dto

data class UserDto(
        val userId: Long,
        val username: String,
        val secret: String,
        val rol: String,
        val personaId: Long,
        val estado: Boolean
)
