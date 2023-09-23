package com.ucb.demo.dao.userRepository

import com.ucb.demo.dao.Persona
import org.springframework.data.repository.CrudRepository

interface PersonaRepository: CrudRepository<Persona, Long> {
}