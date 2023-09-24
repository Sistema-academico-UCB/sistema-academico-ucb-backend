package com.ucb.demo.dto

data class CollegeDto (
        val colegioId: Long,
        val nombreColegio: String,
        val tipo: String,
        val direccion: String,
        val estado: Boolean
)