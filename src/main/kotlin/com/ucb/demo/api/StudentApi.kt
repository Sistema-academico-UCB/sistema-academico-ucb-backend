package com.ucb.demo.api

import com.ucb.demo.bl.StudentBl
import com.ucb.demo.dto.ResponseDto
import com.ucb.demo.dto.ResponseListDto
import com.ucb.demo.dto.StudentDto
import com.ucb.demo.dto.StudentListDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/student")
class StudentApi @Autowired constructor(
        private val studentBl: StudentBl,
){

    // Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(StudentApi::class.java)
    }



    //==============================================================
    // USUARIO - ESTUDIANTE
    /**
     * Endpoint POST para crear un usuario - estudiante
     * @param studentDto
     * @return ResponseDto<Long>
     */
    @PostMapping()
    fun createStudent(@RequestBody studentDto: StudentDto): ResponseDto<Long> {
        LOGGER.info("Iniciando logica para crear un estudiante")
        val studentId = studentBl.createStudent(studentDto)
        if (studentId == 500L) {
            return ResponseDto(
                    success = false,
                    message = "Ya existe un estudiante con el mismo correo y CI",
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
    @GetMapping("/{studentId}")
    fun getStudentById(@PathVariable studentId: Long): ResponseDto<StudentDto> {
        LOGGER.info("Iniciando logica para obtener un estudiante por su id")
        val studentDto = studentBl.getStudentById(studentId)
        return ResponseDto(
                success = true,
                message = "Estudiante obtenido",
                data = studentDto
        )
    }

    /**
     * Endpoint PUT para actualizar un estudiante por su id
     * @param studentDto
     */
    @PutMapping("/{studentId}")
    fun updateStudentById(@PathVariable studentId: Long, @RequestBody studentDto: StudentDto): ResponseDto<String> {
        LOGGER.info("Iniciando logica para actualizar un estudiante por su id")
        studentDto.estudianteId = studentId
        val data = studentBl.updateStudent(studentDto)
        return ResponseDto(
                success = true,
                message = "Estudiante actualizado",
                data = data
        )
    }


    /**
     * Endpoint GET para obtener todos los estudiantes
     * orderBy: apellido_paterno and carnet_identidad
     * filterBy: carnet_identidad, semestre, carrera_id, nombre( nombre, apellido_paterno, apellido_materno)
     * Los campos de tipo cadena (carnet_identidad y nombre) se pueden buscar por coincidencia parcial o absoluta
     * @param page
     * @param size
     * @param carnet_identidad
     * @param semestre
     * @param carrera_id
     * @param nombre
     * @param sortBy
     * @param sortType
     * @return ResponseDto<List<StudentDto>>
     */
    @GetMapping()
    fun getAllStudents(
            @RequestParam(defaultValue = "0") page: Int,
            @RequestParam(defaultValue = "10") size: Int,
            @RequestParam(required = false) carnet_identidad: String?,
            @RequestParam(required = false) semestre: Int?,
            @RequestParam(required = false) carrera_id: Long?,
            @RequestParam(required = false) nombre: String?,
            @RequestParam(defaultValue = "apellido_paterno") sortBy: String,
            @RequestParam(defaultValue = "asc") sortType: String): ResponseListDto<List<StudentListDto>> {
        LOGGER.info("Iniciando logica para obtener todos los estudiantes")
        val returnedList = studentBl.getAllStudents(page, size, carnet_identidad, semestre, carrera_id, nombre, sortBy, sortType)
        val studentDtoList = returnedList[0] as List<StudentListDto>
        val totalElements = returnedList[1] as Long
        return ResponseListDto(
                success = true,
                message = "Estudiantes obtenidos",
                data = studentDtoList,
                totalElements = totalElements
        )
    }



}