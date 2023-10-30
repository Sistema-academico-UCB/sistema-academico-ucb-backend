package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Persona
import org.springframework.data.repository.CrudRepository

interface PersonaRepository: CrudRepository<Persona, Long> {
    //Verificar si existe un registro con el mismo correo
    fun findByCorreoAndCarnetIdentidadAndEstado(correo: String, carnetIdentidad: String, estado: Boolean): Persona

    //Obtener el usuario por el correo electronico
    fun findByCorreoAndEstado(correo: String, estado: Boolean): Persona?

    //Obtener el registro de persona por id
    fun findByPersonaIdAndEstado(personaId: Long, estado: Boolean): Persona?
}