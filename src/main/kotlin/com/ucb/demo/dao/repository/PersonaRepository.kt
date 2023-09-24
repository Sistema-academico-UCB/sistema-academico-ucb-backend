package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Persona
import org.springframework.data.repository.CrudRepository

interface PersonaRepository: CrudRepository<Persona, Long> {
}