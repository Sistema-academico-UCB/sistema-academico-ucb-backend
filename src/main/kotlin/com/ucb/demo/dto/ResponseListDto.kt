package com.ucb.demo.dto

data class ResponseListDto<T> (
        val data: T,
        val message: String,
        val success: Boolean,
        val totalElements: Long
)