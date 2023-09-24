package com.ucb.demo.dao.repository

import com.ucb.demo.dao.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {
}