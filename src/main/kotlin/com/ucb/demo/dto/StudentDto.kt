package com.ucb.demo.dto

import java.util.Date

data class StudentDto(
        /*
        val estudianteId: Long,
        val semestre: Int,
        val tipo: String,
        val userId: Long,
        val colegioId: Long,
        val estado: Boolean*/
        val estudianteId: Long,
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
        val fechaRegistro: String,
        val estadoCivil: String,
        val username: String,
        val secret: String,
        val rol: String,
        val semestre: Int,
        val colegioId: Long,
        val carreraId: Long,
        val estado: Boolean
)