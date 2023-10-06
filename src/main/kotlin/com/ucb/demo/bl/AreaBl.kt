package com.ucb.demo.bl

import com.ucb.demo.dao.repository.CareerRepository
import com.ucb.demo.dao.repository.CollegeRepository
import com.ucb.demo.dao.repository.ProfessionRepository
import com.ucb.demo.dto.CareerDto
import com.ucb.demo.dto.CollegeDto
import com.ucb.demo.dto.ProfessionDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
@Service
class AreaBl @Autowired constructor(
    private val collegeRepository: CollegeRepository,
        private val careerRepository: CareerRepository,
        private val professionRepository: ProfessionRepository,
) {
    // Logger
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AreaBl::class.java.name)
    }

    //==============================================================
    // Método para obtener todos los colegios
    fun getAllColleges(): List<CollegeDto> {
        LOGGER.info("Iniciando logica para obtener todos los colegios")
        val colleges = collegeRepository.findAll()
        LOGGER.info("Se han obtenido todos los colegios")
        val collegesDto: MutableList<CollegeDto> = mutableListOf()
        for (college in colleges) {
            val collegeDto = CollegeDto(
                college.colegioId,
                college.nombreColegio,
                college.tipo,
                college.direccion,
                college.estado
            )
            collegesDto.add(collegeDto)
        }
        return collegesDto
    }

    // Método para obtener un colegio por su id
    fun getCollegeById(collegeId: Long): CollegeDto {
        LOGGER.info("Iniciando logica para obtener un colegio por su id")
        val college = collegeRepository.findByColegioIdAndEstadoIsTrue(collegeId)
        LOGGER.info("Se ha obtenido un colegio por su id")
        return CollegeDto(
                college.colegioId,
                college.nombreColegio,
                college.tipo,
                college.direccion,
                college.estado
        )
    }

    //==============================================================
    // Método para obtener todas las carreras
    fun getAllCareers(): List<CareerDto> {
        LOGGER.info("Iniciando logica para obtener todas las carreras")
        val careers = careerRepository.findAllByCarreraIsTrueAndEstadoIsTrue()
        LOGGER.info("Se han obtenido todas las carreras")
        val careersDto: MutableList<CareerDto> = mutableListOf()
        careers.forEach { career ->
            val careerDto = CareerDto(
                    career.departamentoCarreraId,
                    career.sigla,
                    career.nombre,
                    career.programa,
                    career.carrera,
                    career.estado
            )
            careersDto.add(careerDto)
        }
        return careersDto
    }

    // Método para obtener una carrera por su id
    fun getCareerById(careerId: Long): CareerDto {
        LOGGER.info("Iniciando logica para obtener una carrera por su id")
        val career = careerRepository.findByDepartamentoCarreraIdAndEstadoIsTrue(careerId)
        LOGGER.info("Se ha obtenido una carrera por su id")
        return CareerDto(
                career.departamentoCarreraId,
                career.sigla,
                career.nombre,
                career.programa,
                career.carrera,
                career.estado
        )
    }


    //==============================================================
    // Método para obtener todos los departamentos
    fun getAllDepartments(): List<CareerDto> {
        LOGGER.info("Iniciando logica para obtener todos los departamentos")
        val careers = careerRepository.findAllByCarreraIsFalseAndEstadoIsTrue()
        LOGGER.info("Se han obtenido todos los departamentos")
        val careersDto: MutableList<CareerDto> = mutableListOf()
        careers.forEach { career ->
            val careerDto = CareerDto(
                    career.departamentoCarreraId,
                    career.sigla,
                    career.nombre,
                    career.programa,
                    career.carrera,
                    career.estado
            )
            careersDto.add(careerDto)
        }
        return careersDto
    }

    // Método para obtener un departamento por su id
    fun getDepartmentById(departmentId: Long): CareerDto {
        LOGGER.info("Iniciando logica para obtener un departamento por su id")
        val career = careerRepository.findByDepartamentoCarreraIdAndEstadoIsTrue(departmentId)
        LOGGER.info("Se ha obtenido un departamento por su id")
        return CareerDto(
                career.departamentoCarreraId,
                career.sigla,
                career.nombre,
                career.programa,
                career.carrera,
                career.estado
        )
    }

    //==============================================================
    // Método para obtener todas las profesiones
    fun getAllProfessions(): List<ProfessionDto> {
        LOGGER.info("Iniciando logica para obtener todas las profesiones")
        val professions = professionRepository.findAllByEstadoIsTrue()
        LOGGER.info("Se han obtenido todas las profesiones")
        val professionsDto: MutableList<ProfessionDto> = mutableListOf()
        professions.forEach { profession ->
            val professionDto = ProfessionDto(
                    profession.profesionId,
                    profession.nombreProfesion,
                    profession.estado
            )
            professionsDto.add(professionDto)
        }
        return professionsDto
    }

    // Método para obtener una profesion por su id
    fun getProfessionById(professionId: Long): ProfessionDto {
        LOGGER.info("Iniciando logica para obtener una profesion por su id")
        val profession = professionRepository.findByProfesionIdAndEstadoIsTrue(professionId)
        LOGGER.info("Se ha obtenido una profesion por su id")
        return ProfessionDto(
                profession.profesionId,
                profession.nombreProfesion,
                profession.estado
        )
    }

}