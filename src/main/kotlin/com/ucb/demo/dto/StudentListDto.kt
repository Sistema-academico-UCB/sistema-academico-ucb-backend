package com.ucb.demo.dto

import java.util.Date

data class StudentListDto(
        var userId: Long,
        var estudianteId: Long,
        val nombre: String,
        val apellidoPaterno: String,
        val apellidoMaterno: String,
        val carnetIdentidad: String,
        val fechaNacimiento: Date,
        val correo: String,
        val genero: String,
        val celular: String,
        val descripcion: String,
        val uuidFoto: String,
        val uuidPortada: String,
        val direccion: String,
        val fechaRegistro: Date,
        val estadoCivil: String,
        val username: String,
        val rol: String,
        val semestre: Int,
        val colegioId: Long,
        val carreraId: Long,
        val estado: Boolean
)
