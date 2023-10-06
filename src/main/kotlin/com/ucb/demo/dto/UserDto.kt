package com.ucb.demo.dto

import java.util.Date

open class UserDto (
        open val userId: Long,
        open val username: String,
        open val secret: String,
        open val rol: String,
        open val personaId: Long,
        //
        open val nombre: String,
        open val apellidoPaterno: String,
        open val apellidoMaterno: String,
        open val carnetIdentidad: String,
        open val fechaNacimiento: Date,
        open val correo: String,
        open val genero: String,
        open val celular: String,
        open val descripcion: String,
        open val uuidFoto: String,
        open val uuidPortada: String,
        open val direccion: String,
        open val fechaRegistro: Date,
        open val estadoCivil: String,
        open val estado: Boolean
)