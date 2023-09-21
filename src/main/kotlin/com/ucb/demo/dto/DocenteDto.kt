package com.ucb.demo.dto

data class DocenteDto(
        val docenteId: Long,
        val directorDeCarrera: Boolean,
        val tipo: String,
        val userId: Long,
        val estado: Boolean
)
