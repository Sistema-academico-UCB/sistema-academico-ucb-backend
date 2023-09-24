package com.ucb.demo.api

import com.ucb.demo.bl.AreaBl
import com.ucb.demo.dto.CareerDto
import com.ucb.demo.dto.CollegeDto
import com.ucb.demo.dto.ProfessionDto
import com.ucb.demo.dto.ResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/")
class AreaApi @Autowired constructor(
        private val areaBl: AreaBl
){
    // Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(UserApi::class.java)
    }

    //==============================================================
    // AREA - COLEGIO
    /**
     * Endpoint GET para obtener todos los colegios
     * @return ResponseDto<List<CollegeDto>>
     */
    @GetMapping("/colleges")
    fun getAllColleges(): ResponseDto<List<CollegeDto>> {
        AreaApi.LOGGER.info("Iniciando logica para obtener todos los colegios")
        val colleges = areaBl.getAllColleges()
        return ResponseDto(
                success = true,
                message = "Colegios obtenidos",
                data = colleges
        )
    }

    //==============================================================
    // AREA - CARRERA
    /**
     * Endpoint GET para obtener todas las carreras
     * @return ResponseDto<List<CareerDto>>
     */
    @GetMapping("/careers")
    fun getAllCareers(): ResponseDto<List<CareerDto>> {
        AreaApi.LOGGER.info("Iniciando logica para obtener todas las carreras")
        val careers = areaBl.getAllCareers()
        return ResponseDto(
                success = true,
                message = "Carreras obtenidas",
                data = careers
        )
    }

    //==============================================================
    // AREA - DEPARTAMENTO
    /**
     * Endpoint GET para obtener todos los departamentos
     * @return ResponseDto<List<CareerDto>>
     */
    @GetMapping("/departments")
    fun getAllDepartments(): ResponseDto<List<CareerDto>> {
        AreaApi.LOGGER.info("Iniciando logica para obtener todos los departamentos")
        val departments = areaBl.getAllDepartments()
        return ResponseDto(
                success = true,
                message = "Departamentos obtenidos",
                data = departments
        )
    }

    //==============================================================
    // AREA - PROFESION
    /**
     * Endpoint GET para obtener todas las profesiones
     * @return ResponseDto<List<ProfessionDto>>
     */
    @GetMapping("/professions")
    fun getAllProfessions(): ResponseDto<List<ProfessionDto>> {
        AreaApi.LOGGER.info("Iniciando logica para obtener todas las profesiones")
        val professions = areaBl.getAllProfessions()
        return ResponseDto(
                success = true,
                message = "Profesiones obtenidas",
                data = professions
        )
    }



}