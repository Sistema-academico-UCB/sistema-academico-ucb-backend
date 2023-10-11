package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Friend
import org.springframework.data.repository.CrudRepository

interface FriendRepository: CrudRepository<Friend, Long> {
    fun findAllByUsuarioIdUsuarioOrAmigoIdUsuario(usuarioIdUsuario: Long, amigoIdUsuario: Long): List<Friend>
}