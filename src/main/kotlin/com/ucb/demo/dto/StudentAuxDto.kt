package com.ucb.demo.dto

import java.util.*

data class StudentAuxDto(
        override val userId: Long,
        override val username: String,
        override val secret: String,
        override val rol: String,
        override val personaId: Long,
        override val nombre: String,
        override val apellidoPaterno: String,
        override val apellidoMaterno: String,
        override val carnetIdentidad: String,
        override val fechaNacimiento: Date,
        override val correo: String,
        override val genero: String,
        override val celular: String,
        override val descripcion: String,
        override val uuidFoto: String,
        override val uuidPortada: String,
        override val direccion: String,
        override val fechaRegistro: Date,
        override val estadoCivil: String,
        override val estado: Boolean,
        val estudianteId: Long,
        val semestre: Int,
        val tipo: String,
        val colegioId: Long,
): UserDto(userId, username, secret, rol, personaId, nombre, apellidoPaterno, apellidoMaterno, carnetIdentidad, fechaNacimiento, correo, genero, celular, descripcion, uuidFoto, uuidPortada, direccion, fechaRegistro, estadoCivil, estado)