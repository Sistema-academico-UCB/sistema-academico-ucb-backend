package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Teacher
import org.springframework.data.repository.CrudRepository

interface TeacherRepository: CrudRepository<Teacher, Long> {
    fun existsByUserIdAndEstado(userId: Long, estado: Boolean): Boolean
}