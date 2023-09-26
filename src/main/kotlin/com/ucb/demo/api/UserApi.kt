package com.ucb.demo.api
import org.springframework.beans.factory.annotation.Autowired
import com.ucb.demo.bl.StudentBl
import com.ucb.demo.bl.TeacherBl
import com.ucb.demo.dto.ResponseDto
import com.ucb.demo.dto.StudentDto
import com.ucb.demo.dto.TeacherDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/")
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
        if (studentId == 500L) {
            return ResponseDto(
                    success = false,
                    message = "Ya existe un estudiante con el mismo correo",
                    data = studentId
            )
        }
        return ResponseDto(
                success = true,
                message = "Estudiante creado",
                data = studentId
        )
    }

    /**
     * Endpoint GET para obtener un estudiante por su id
     * @param studentId
     * @return ResponseDto<StudentDto>
     */
    @GetMapping("/student/{studentId}")
    fun getStudentById(@PathVariable studentId: Long): ResponseDto<StudentDto> {
        UserApi.LOGGER.info("Iniciando logica para obtener un estudiante por su id")
        val studentDto = studentBl.getStudentById(studentId)
        return ResponseDto(
                success = true,
                message = "Estudiante obtenido",
                data = studentDto
        )
    }

    /**
     * Endpoint PUT para actualizar un estudiante por su id
     * @param studentId
     * @param studentDto
     */
    @PutMapping("/student/{studentId}")
    fun updateStudentById(@PathVariable studentId: Long, @RequestBody studentDto: StudentDto): ResponseDto<StudentDto> {
        UserApi.LOGGER.info("Iniciando logica para actualizar un estudiante por su id")
        //studentBl.updateStudentById(studentId, studentDto)
        return ResponseDto(
                success = true,
                message = "Estudiante actualizado",
                data = studentDto
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
        if (teacherId == 500L) {
            return ResponseDto(
                    success = false,
                    message = "Ya existe un docente con el mismo correo",
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
    @GetMapping("/teacher/{teacherId}")
    fun getTeacherById(@PathVariable teacherId: Long): ResponseDto<TeacherDto> {
        UserApi.LOGGER.info("Iniciando logica para obtener un docente por su id")
        val teacherDto = teacherBl.getTeacherById(teacherId)
        return ResponseDto(
                success = true,
                message = "Docente obtenido",
                data = teacherDto
        )
    }



}