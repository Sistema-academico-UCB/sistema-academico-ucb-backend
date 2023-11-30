package com.ucb.demo.dao.repository

import org.springframework.data.repository.CrudRepository
import com.ucb.demo.dao.TeacherDepartment

interface TeacherDepartmentRepository: CrudRepository<TeacherDepartment, Long> {
    //Deberia devolver solo un registro de la carrera activa, las terminadas no deberian aparecer porque tienen estado false
    fun findByDocenteIdAndEstado(docenteId: Long, estado: Boolean): List<TeacherDepartment>?

}