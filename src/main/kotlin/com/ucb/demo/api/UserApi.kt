package com.ucb.demo.api
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
import com.ucb.demo.bl.StudentBl
import com.ucb.demo.bl.TeacherBl
import com.ucb.demo.dto.ResponseDto
import com.ucb.demo.dto.StudentDto
import com.ucb.demo.dto.TeacherDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@RestController
@RequestMapping("/api/v1/user")
class UserApi @Autowired constructor(
        private val studentBl: StudentBl,
        private val teacherBl: TeacherBl
){
    // Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(UserApi::class.java)
    }

    //==============================================================
    // USUARIO - ESTUDIANTE
    /**
     * Endpoint POST para crear un usuario - estudiante
     * @param studentDto
     * @return ResponseDto<Long>
     */
    @PostMapping("/student")
    fun createStudent(@RequestBody studentDto: StudentDto): ResponseDto<Long> {
        UserApi.LOGGER.info("Iniciando logica para crear un estudiante")
        val studentId = studentBl.createStudent(studentDto)
        return ResponseDto(
                success = true,
                message = "Estudiante creado",
                data = studentId
        )
    }


    //==============================================================
    // USUARIO - DOCENTE
    /**
     * Endpoint POST para crear un usuario - docente
     * @param teacherDto
     * @return ResponseDto<Long>
     */
    @PostMapping("/teacher")
    fun createTeacher(@RequestBody teacherDto: TeacherDto): ResponseDto<Long> {
        UserApi.LOGGER.info("Iniciando logica para crear un docente")
        val teacherId = teacherBl.createTeacher(teacherDto)
        return ResponseDto(
                success = true,
                message = "Docente creado",
                data = teacherId
        )
    }

}