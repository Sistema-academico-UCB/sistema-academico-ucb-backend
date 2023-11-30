package com.ucb.demo.api

import com.ucb.demo.bl.AreaBl
import com.ucb.demo.dto.CareerDto
import com.ucb.demo.dto.CollegeDto
import com.ucb.demo.dto.ProfessionDto
import com.ucb.demo.dto.ResponseDto
import com.ucb.demo.util.UcbException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping

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
        LOGGER.info("Iniciando logica para obtener todos los colegios")
        val colleges = areaBl.getAllColleges()
        return ResponseDto(
                success = true,
                message = "Colegios obtenidos",
                data = colleges
        )
    }

    /**
     * Endpoint GET para obtener un colegio por su id
     * @param collegeId
     * @return ResponseDto<CollegeDto>
     */
    @GetMapping("/colleges/{collegeId}")
    fun getCollegeById(@PathVariable collegeId: Long): ResponseDto<CollegeDto> {
        LOGGER.info("Iniciando logica para obtener un colegio por su id")
        val college = areaBl.getCollegeById(collegeId)
        return ResponseDto(
                success = true,
                message = "Colegio obtenido",
                data = college
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
        LOGGER.info("Iniciando logica para obtener todas las carreras")
        val careers = areaBl.getAllCareers()
        return ResponseDto(
                success = true,
                message = "Carreras obtenidas",
                data = careers
        )
    }

    /**
     * Endpoint GET para obtener una carrera por su id
     * @param careerId
     * @return ResponseDto<CareerDto>
     */
    @GetMapping("/careers/{careerId}")
    fun getCareerById(@PathVariable careerId: Long): ResponseDto<CareerDto> {
        LOGGER.info("Iniciando logica para obtener una carrera por su id")
        val career = areaBl.getCareerById(careerId)
        return ResponseDto(
                success = true,
                message = "Carrera obtenida",
                data = career
        )
    }

    /**
     * Endpoint POST para crear una carrera
     * @param careerDto
     * @return ResponseDto<Long>
     */
    @PostMapping("/careers")
    fun createCareer(@RequestBody careerDto: CareerDto): ResponseDto<Long> {
        LOGGER.info("Iniciando logica para crear una carrera")
        return try {
            val careerId = areaBl.createCareer(careerDto)
            ResponseDto(
                    success = true,
                    message = "Carrera creada",
                    data = careerId
            )
        } catch (ex: UcbException) {
            ResponseDto(
                    success = false,
                    message = ex.message!!,
                    data = null
            )
        }
    }

    /**
     * Endpoint PUT para actualizar una carrera por su id
     * @param careerId
     * @param careerDto
     * @return ResponseDto<String>
     */
    @PutMapping("/careers/{careerId}")
    fun updateCareerById(@PathVariable careerId: Long, @RequestBody careerDto: CareerDto): ResponseDto<String> {
        LOGGER.info("Iniciando logica para actualizar una carrera por su id")
        return try {
            careerDto.carreraId = careerId
            val career = areaBl.updateCareerById(careerDto, careerId)
            ResponseDto(
                    success = true,
                    message = "Carrera actualizada",
                    data = career
            )
        }catch (ex: UcbException){
            ResponseDto(
                    success = false,
                    message = ex.message!!,
                    data = null
            )
        }   
    }

    /**
     * Endpoint DELETE para eliminar una carrera por su id de forma logica
     * @param careerId
     * @return ResponseDto<String>
     */
    @DeleteMapping("/careers/{careerId}")
    fun deleteCareerById(@PathVariable careerId: Long): ResponseDto<String> {
        LOGGER.info("Iniciando logica para eliminar una carrera por su id")
        return try {
            val career = areaBl.deleteCareerById(careerId)
            ResponseDto(
                success = true,
                message = "Carrera eliminada",
                data = career
            )
        } catch (ex: UcbException) {
            ResponseDto(
                    success = false,
                    message = ex.message!!,
                    data = null
            )
        }
        
    }


    //==============================================================
    // AREA - DEPARTAMENTO
    /**
     * Endpoint GET para obtener todos los departamentos
     * @return ResponseDto<List<CareerDto>>
     */
    @GetMapping("/departments")
    fun getAllDepartments(): ResponseDto<List<CareerDto>> {
        LOGGER.info("Iniciando logica para obtener todos los departamentos")
        val departments = areaBl.getAllDepartments()
        return ResponseDto(
                success = true,
                message = "Departamentos obtenidos",
                data = departments
        )
    }

    /**
     * Endpoint GET para obtener un departamento por su id
     * @param departmentId
     * @return ResponseDto<CareerDto>
     */
    @GetMapping("/departments/{departmentId}")
    fun getDepartmentById(@PathVariable departmentId: Long): ResponseDto<CareerDto> {
        LOGGER.info("Iniciando logica para obtener un departamento por su id")
        val department = areaBl.getDepartmentById(departmentId)
        return ResponseDto(
                success = true,
                message = "Departamento obtenido",
                data = department
        )
    }

    /**
     * Endpoint POST para crear un departamento
     * @param careerDto
     * @return ResponseDto<Long>
     */
    @PostMapping("/departments")
    fun createDepartment(@RequestBody departmentDto: CareerDto): ResponseDto<Long> {
        LOGGER.info("Iniciando logica para crear un departamento")
        return try {
            val departmentId = areaBl.createCareer(departmentDto)
            ResponseDto(
                    success = true,
                    message = "Departamento creado",
                    data = departmentId
            )
        } catch (ex: UcbException) {
            ResponseDto(
                    success = false,
                    message = ex.message!!,
                    data = null
            )
        }
    }

    /**
     * Endpoint PUT para actualizar un departamento por su id
     * @param careerId
     * @param careerDto
     * @return ResponseDto<String>
     */
    @PutMapping("/departments/{departmentId}")
    fun updateDepartmentById(@PathVariable departmentId: Long, @RequestBody careerDto: CareerDto): ResponseDto<String> {
        LOGGER.info("Iniciando logica para actualizar un departamento por su id")
        return try {
            careerDto.carreraId = departmentId
            val career = areaBl.updateCareerById(careerDto, departmentId)
            ResponseDto(
                    success = true,
                    message = "Departemento actualizado",
                    data = career
            )
        }catch (ex: UcbException){
            ResponseDto(
                    success = false,
                    message = ex.message!!,
                    data = null
            )
        }   
    }

    /**
     * Endpoint DELETE para eliminar un departamento por su id de forma logica
     * @param careerId
     * @return ResponseDto<String>
     */
    @DeleteMapping("/departments/{departmentId}")
    fun deleteDepartmentById(@PathVariable departmentId: Long): ResponseDto<String> {
        LOGGER.info("Iniciando logica para eliminar un departamento por su id")
        return try {
            val career = areaBl.deleteCareerById(departmentId)
            ResponseDto(
                success = true,
                message = "Departamento eliminado",
                data = career
            )
        } catch (ex: UcbException) {
            ResponseDto(
                    success = false,
                    message = ex.message!!,
                    data = null
            )
        }
        
    }

    //==============================================================
    // AREA - PROFESION
    /**
     * Endpoint GET para obtener todas las profesiones
     * @return ResponseDto<List<ProfessionDto>>
     */
    @GetMapping("/professions")
    fun getAllProfessions(): ResponseDto<List<ProfessionDto>> {
        LOGGER.info("Iniciando logica para obtener todas las profesiones")
        val professions = areaBl.getAllProfessions()
        return ResponseDto(
                success = true,
                message = "Profesiones obtenidas",
                data = professions
        )
    }

    /**
     * Endpoint GET para obtener una profesion por su id
     * @param professionId
     * @return ResponseDto<ProfessionDto>
     */
    @GetMapping("/professions/{professionId}")
    fun getProfessionById(@PathVariable professionId: Long): ResponseDto<ProfessionDto> {
        LOGGER.info("Iniciando logica para obtener una profesion por su id")
        val profession = areaBl.getProfessionById(professionId)
        return ResponseDto(
                success = true,
                message = "Profesion obtenida",
                data = profession
        )
    }

    /**
     * Endpoint POST para crear una profesion
     * @param professionDto
     * @return ResponseDto<Long>
     */
    @PostMapping("/professions")
    fun createProfession(@RequestBody professionDto: ProfessionDto): ResponseDto<Long> {
        LOGGER.info("Iniciando logica para crear una profesion")
        return try {
            val professionId = areaBl.createProfession(professionDto)
            ResponseDto(
                    success = true,
                    message = "Profesion creada",
                    data = professionId
            )
        } catch (ex: UcbException) {
            ResponseDto(
                    success = false,
                    message = ex.message!!,
                    data = null
            )
        }
    }

    /**
     * Endpoint PUT para actualizar una profesion por su id
     * @param professionId
     * @param professionDto
     * @return ResponseDto<String>
     */
    @PutMapping("/professions/{professionId}")
    fun updateProfessionById(@PathVariable professionId: Long, @RequestBody professionDto: ProfessionDto): ResponseDto<String> {
        LOGGER.info("Iniciando logica para actualizar una profesion por su id")
        return try {
            professionDto.profesionId = professionId
            val profession = areaBl.updateProfessionById(professionDto, professionId)
            ResponseDto(
                    success = true,
                    message = "Profesion actualizada",
                    data = profession
            )
        }catch (ex: UcbException){
            ResponseDto(
                    success = false,
                    message = ex.message!!,
                    data = null
            )
        }   
    }

    /**
     * Endpoint DELETE para eliminar una profesion por su id de forma logica
     * @param professionId
     * @return ResponseDto<String>
     */
    @DeleteMapping("/professions/{professionId}")
    fun deleteProfessionById(@PathVariable professionId: Long): ResponseDto<String> {
        LOGGER.info("Iniciando logica para eliminar una profesion por su id")
        return try {
            val profession = areaBl.deleteProfessionById(professionId)
            ResponseDto(
                success = true,
                message = "Profesion eliminada",
                data = profession
            )
        } catch (ex: UcbException) {
            ResponseDto(
                    success = false,
                    message = ex.message!!,
                    data = null
            )
        }
        
    }

}