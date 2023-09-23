package com.ucb.demo.dao.userRepository

import com.ucb.demo.dao.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {
}