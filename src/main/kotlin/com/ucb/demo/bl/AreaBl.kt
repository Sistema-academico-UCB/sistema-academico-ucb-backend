package com.ucb.demo.bl

import com.ucb.demo.dao.repository.CareerRepository
import com.ucb.demo.dao.repository.CollegeRepository
import com.ucb.demo.dao.repository.ProfessionRepository
import com.ucb.demo.dao.Career
import com.ucb.demo.dto.CareerDto
import com.ucb.demo.dto.CollegeDto
import com.ucb.demo.dto.ProfessionDto
import com.ucb.demo.util.UcbException
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
    //=========================CARREER=============================//
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

    // Método para crear una carrera
    fun createCareer(careerDto: CareerDto): Long {
        if(careerDto.carrera){
            LOGGER.info("BL - Iniciando logica para crear una carrera")
        }else{
            LOGGER.info("BL - Iniciando logica para crear un departamento")
        }
        try {
            val career = Career()
            career.sigla = careerDto.sigla
            career.nombre = careerDto.nombre
            career.programa = careerDto.programa
            career.carrera = careerDto.carrera
            career.estado = careerDto.estado
            val careerCreated = careerRepository.save(career)
            // Verificamos si es una carrera o un departamento
            if(careerDto.carrera){
                LOGGER.info("BL - Se ha creado una carrera")
            }else{
                LOGGER.info("BL - Se ha creado un departamento")
            }
            return careerCreated.departamentoCarreraId
        } catch (e: Exception) {
            // Verificamos si es una carrera o un departamento
            if(careerDto.carrera){
                LOGGER.error("BL - Error al crear una carrera: ${e.message}")
                throw UcbException("Error al crear la carrera")
            }else{
                LOGGER.error("BL - Error al crear un departamento: ${e.message}")
                throw UcbException("Error al crear el departamento")
            }
        }
    }

    // Método para actualizar una carrera por su id
    fun updateCareerById(careerDto: CareerDto, careerId: Long): String {
        // Verificamos si es una carrera o un departamento
        if(careerDto.carrera){
            LOGGER.info("BL - Iniciando logica para actualizar una carrera por su id")
        }else{
            LOGGER.info("BL - Iniciando logica para actualizar un departamento por su id")
        }
        val career = careerRepository.findByDepartamentoCarreraIdAndEstadoIsTrue(careerId)
        career.sigla = careerDto.sigla
        career.nombre = careerDto.nombre
        career.programa = careerDto.programa
        career.carrera = careerDto.carrera
        career.estado = careerDto.estado
        careerRepository.save(career)
        // Verificamos si es una carrera o un departamento
        if(careerDto.carrera){
            LOGGER.info("BL - Se ha actualizado una carrera por su id")
            return "Carrera actualizada"
        }else{
            LOGGER.info("BL - Se ha actualizado un departamento por su id")
            return "Departamento actualizado"
        }
        
    }

    // Método para eliminar de manera lógica una carrera por su id
    fun deleteCareerById(careerId: Long): String {
        // Verificamos si es una carrera o un departamento
        
        LOGGER.info("BL - Iniciando logica para eliminar de manera logica una carrera/departamento por su id")
        val career = careerRepository.findByDepartamentoCarreraIdAndEstadoIsTrue(careerId)
        //Verificamos si hay registros en la tabla de carrera_estudiante con este id
        if (career.carrerasEstudiante.isNotEmpty()) {
            // Verificamos si es una carrera o un departamento
            if(career.carrera){
                LOGGER.info("BL - No se puede eliminar la carrera porque tiene estudiantes asociados")
                throw UcbException("No se puede eliminar la carrera porque tiene estudiantes asociados")
            }else{
                LOGGER.info("BL - No se puede eliminar el departamento porque tiene docentes asociados")
                throw UcbException("No se puede eliminar el departamento porque tiene docentes asociados")
            }
        }
        career.estado = false
        careerRepository.save(career)
        // Verificamos si es una carrera o un departamento
        if(career.carrera){
            LOGGER.info("BL - Se ha eliminado de manera logica una carrera por su id")
            return "Carrera eliminada"
        }else{
            LOGGER.info("BL - Se ha eliminado de manera logica un departamento por su id")
            return "Departamento eliminado"
        }
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