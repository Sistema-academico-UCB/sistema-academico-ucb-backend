package com.ucb.demo.api

import com.ucb.demo.bl.TeacherBl
import com.ucb.demo.dto.ResponseDto
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

}