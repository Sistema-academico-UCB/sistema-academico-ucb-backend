package com.ucb.demo.dao.repository

import com.ucb.demo.dao.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {
    fun findByPersonaIdAndEstado(personaId: Long, estado: Boolean): User

    fun findByUserIdAndEstado(userId: Long, estado: Boolean): User?

}