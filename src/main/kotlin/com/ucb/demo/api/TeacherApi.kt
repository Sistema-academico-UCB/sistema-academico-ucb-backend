package com.ucb.demo.api

import com.ucb.demo.bl.TeacherBl
import com.ucb.demo.dto.ResponseDto
import com.ucb.demo.dto.StudentDto
import com.ucb.demo.dto.TeacherDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/teacher")
class TeacherApi @Autowired constructor(
        private val teacherBl: TeacherBl,
){

    // Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(TeacherApi::class.java)
    }

    //==============================================================
    // USUARIO - DOCENTE
    /**
     * Endpoint POST para crear un usuario - docente
     * @param teacherDto
     * @return ResponseDto<Long>
     */
    @PostMapping()
    fun createTeacher(@RequestBody teacherDto: TeacherDto): ResponseDto<Long> {
        LOGGER.info("Iniciando logica para crear un docente")
        val teacherId = teacherBl.createTeacher(teacherDto)
        if (teacherId == 500L) {
            return ResponseDto(
                    success = false,
                    message = "Ya existe un docente con el mismo correo y CI",
                    data = teacherId
            )
        }
        return ResponseDto(
                success = true,
                message = "Docente creado",
                data = teacherId
        )
    }

    /**
     * Endpoint GET para obtener un docente por su id
     * @param teacherId
     * @return ResponseDto<TeacherDto>
     */
    @GetMapping("/{teacherId}")
    fun getTeacherById(@PathVariable teacherId: Long): ResponseDto<TeacherDto> {
        LOGGER.info("Iniciando logica para obtener un docente por su id")
        val teacherDto = teacherBl.getTeacherById(teacherId)

        return ResponseDto(
                success = true,
                message = "Docente obtenido",
                data = teacherDto
        )
    }

    /**
     * Endpoint PUT para actualizar un docente por su id
     * @param teacherDto
     */
    @PutMapping("/{teacherId}")
    fun updateTeacherById(@PathVariable teacherId: Long, @RequestBody teacherDto: TeacherDto): ResponseDto<String> {
        LOGGER.info("Iniciando logica para actualizar un docente por su id")
        teacherDto.docenteId = teacherId
        val data = teacherBl.updateTeacher(teacherDto)
        return ResponseDto(
                success = true,
                message = "Docente actualizado",
                data = data
        )

    }

    /**
     * Endpoint GET para obtener todos los docentes
     * orderBy: apellido_paterno and carnet_identidad
     * filterBy: carnet_identidad, nombre( nombre, apellido_paterno, apellido_materno), departamento_id
     * Los campos de tipo cadena (carnet_identidad y nombre) se pueden buscar por coincidencia parcial o absoluta
     * @param page
     * @param size
     * @param carnet_identidad
     * @param departamento_id
     * @param nombre
     * @param sortBy
     * @param sortType
     * @return ResponseDto<List<TeacherDto>>
     */
    @GetMapping()
    fun getAllTeachers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) carnet_identidad: String?,
        @RequestParam(required = false) departamento_id: Long?,
        @RequestParam(required = false) nombre: String?,
        @RequestParam(defaultValue = "apellido_paterno") sortBy: String,
        @RequestParam(defaultValue = "asc") sortType: String): ResponseDto<List<TeacherDto>> {
        LOGGER.info("Iniciando logica para obtener todos los docentes")
        val teacherDtoList = teacherBl.getAllTeachers(page, size, carnet_identidad, departamento_id, nombre, sortBy, sortType)
        return ResponseDto(
            success = true,
            message = "Docentes obtenidos",
            data = teacherDtoList
        )
    }



}