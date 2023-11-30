package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Career
import org.springframework.data.repository.CrudRepository

interface CareerRepository: CrudRepository<Career, Long> {
    //Get all careers, "carera" and "estado" are true
    fun findAllByCarreraIsTrueAndEstadoIsTrue(): List<Career>

    //Get all departments, "carrera" is false and "estado" is true
    fun findAllByCarreraIsFalseAndEstadoIsTrue(): List<Career>

    fun findByDepartamentoCarreraIdAndEstadoIsTrue(carreraId: Long): Career

    //Obtenemos el ultimo id de la tabla
    fun findTopByOrderByDepartamentoCarreraIdDesc(): Career

}