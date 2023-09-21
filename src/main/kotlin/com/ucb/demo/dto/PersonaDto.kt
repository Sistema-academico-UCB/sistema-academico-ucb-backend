package com.ucb.demo.dto

import java.util.Date

data class PersonaDto (
        val idPersona: Long,
        val nombre: String,
        val apellidoPaterno: String,
        val apellidoMaterno: String,
        val carnetIdentidad: String,
        val fechaNacimiento: String,
        val correo: String,
        val genero: String,
        val celular: String,
        val descripcion: String,
        val uuidFoto: String,
        val direccion: String,
        val fechaRegistro: Date,
        val estadoCivil: String,
        val estado: Boolean
)