package com.ucb.demo.dto

data class CareerDto(
        var carreraId: Long,
        val sigla: String,
        val nombre: String,
        val programa: String,
        val carrera: Boolean,
        val estado: Boolean
)