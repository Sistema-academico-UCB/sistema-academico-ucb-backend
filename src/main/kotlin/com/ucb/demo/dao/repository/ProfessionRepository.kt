package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Profession
import org.springframework.data.repository.CrudRepository

interface ProfessionRepository: CrudRepository<Profession, Long> {
    fun findAllByEstadoIsTrue(): List<Profession>

    fun findByProfesionIdAndEstadoIsTrue(profesionId: Long): Profession
}