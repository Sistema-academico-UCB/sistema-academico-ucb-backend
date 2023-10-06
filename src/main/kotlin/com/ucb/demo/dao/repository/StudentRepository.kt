package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Student
import org.springframework.data.repository.CrudRepository

interface StudentRepository: CrudRepository<Student, Long> {
    fun existsByUserIdAndEstado(userId: Long, estado: Boolean): Boolean

    fun findByUserIdAndEstado(userId: Long, estado: Boolean): Student

}