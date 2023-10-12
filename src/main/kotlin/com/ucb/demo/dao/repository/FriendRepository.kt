package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Friend
import org.springframework.data.repository.CrudRepository

interface FriendRepository: CrudRepository<Friend, Long> {
    fun findAllByUsuarioIdUsuarioOrAmigoIdUsuarioAndAceptadoIsTrue(usuarioIdUsuario: Long, amigoIdUsuario: Long): List<Friend>?
    fun findByUsuarioIdUsuarioAndAmigoIdUsuarioAndAceptadoIsTrue(usuarioIdUsuario: Long, amigoIdUsuario: Long): Friend?
    fun findByUsuarioIdUsuarioAndAmigoIdUsuarioAndAceptadoIsFalse(usuarioIdUsuario: Long, amigoIdUsuario: Long): Friend?
    fun findAllByUsuarioIdUsuarioAndAceptadoIsTrue(usuarioIdUsuario: Long): List<Friend>?
    fun findAllByAmigoIdUsuarioAndAceptadoIsTrue(amigoIdUsuario: Long): List<Friend>?

}