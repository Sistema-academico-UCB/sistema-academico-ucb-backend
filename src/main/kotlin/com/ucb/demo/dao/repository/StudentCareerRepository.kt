package com.ucb.demo.dao.repository

import com.ucb.demo.dao.StudentCareer
import org.springframework.data.repository.CrudRepository

interface StudentCareerRepository: CrudRepository<StudentCareer, Long>{
    //Deberia devolver solo un registro de la carrera activa, las terminadas no deberian aparecer porque tienen estado false
    fun findByEstudianteIdAndEstado(estudianteId: Long, estado: Boolean): List<StudentCareer>?
}