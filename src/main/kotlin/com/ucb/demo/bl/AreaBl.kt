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
        val LOGGER: Logger = LoggerFactory.getLogger(StudentBl::class.java.name)
    }

    //==============================================================
    // Método para obtener todos los colegios
    fun getAllColleges(): List<CollegeDto> {
        AreaBl.LOGGER.info("Iniciando logica para obtener todos los colegios")
        val colleges = collegeRepository.findAll()
        AreaBl.LOGGER.info("Se han obtenido todos los colegios")
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

    //==============================================================
    // Método para obtener todas las carreras
    fun getAllCareers(): List<CareerDto> {
        AreaBl.LOGGER.info("Iniciando logica para obtener todas las carreras")
        val careers = careerRepository.findAllByCarreraIsTrueAndEstadoIsTrue()
        AreaBl.LOGGER.info("Se han obtenido todas las carreras")
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


    //==============================================================
    // Método para obtener todos los departamentos
    fun getAllDepartments(): List<CareerDto> {
        AreaBl.LOGGER.info("Iniciando logica para obtener todos los departamentos")
        val careers = careerRepository.findAllByCarreraIsFalseAndEstadoIsTrue()
        AreaBl.LOGGER.info("Se han obtenido todos los departamentos")
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

    //==============================================================
    // Método para obtener todas las profesiones
    fun getAllProfessions(): List<ProfessionDto> {
        AreaBl.LOGGER.info("Iniciando logica para obtener todas las profesiones")
        val professions = professionRepository.findAllByEstadoIsTrue()
        AreaBl.LOGGER.info("Se han obtenido todas las profesiones")
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

}