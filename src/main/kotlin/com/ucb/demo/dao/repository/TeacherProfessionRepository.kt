package com.ucb.demo.dao.repository

import org.springframework.data.repository.CrudRepository
import com.ucb.demo.dao.TeacherProfession


interface TeacherProfessionRepository: CrudRepository<TeacherProfession, Long> {
    //Deberia devolver solo un registro de la profesion activa, las terminadas no deberian aparecer porque tienen estado false
    fun findByDocenteId(docenteId: Long): List<TeacherProfession>?

}