package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Student
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PagingRepository: JpaRepository <Student, Long>{
    fun findAllByEstado(estado:Boolean, pageable: Pageable): Page<Student>

}