package com.ucb.demo.dao.userRepository

import com.ucb.demo.dao.Usuario
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<Usuario, Long> {
}