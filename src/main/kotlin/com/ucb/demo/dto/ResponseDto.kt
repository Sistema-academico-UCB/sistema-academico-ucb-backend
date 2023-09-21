package com.ucb.demo.dto

data class ResponseDto<T> (
        val data: T,
        val message: String,
        val success: Boolean
)