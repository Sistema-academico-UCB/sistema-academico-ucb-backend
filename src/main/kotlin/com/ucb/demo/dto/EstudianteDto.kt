package com.ucb.demo.dto

data class EstudianteDto(
        val estudianteId: Long,
        val semestre: Int,
        val tipo: String,
        val userId: Long,
        val colegioId: Long,
        val estado: Boolean
)
