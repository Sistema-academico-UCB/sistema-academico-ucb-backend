package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Teacher
import org.springframework.data.repository.CrudRepository

interface TeacherRepository: CrudRepository<Teacher, Long> {
    fun existsByUserIdAndEstado(userId: Long, estado: Boolean): Boolean

    //Obtener el registro de docente por id de usuario
    fun findByUserIdAndEstado(userId: Long, estado: Boolean): Teacher
}