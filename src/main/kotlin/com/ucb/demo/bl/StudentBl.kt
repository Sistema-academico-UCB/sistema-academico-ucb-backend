package com.ucb.demo.bl
import com.ucb.demo.dao.userRepository.PersonaRepository
import com.ucb.demo.dao.userRepository.StudentRepository
import com.ucb.demo.dao.userRepository.UserRepository
import com.ucb.demo.dao.Persona
import com.ucb.demo.dao.Student
import com.ucb.demo.dao.User
import com.ucb.demo.dto.ResponseDto
import com.ucb.demo.dto.StudentDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentBl @Autowired constructor(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val personaRepository: PersonaRepository

){
    // Logger
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(StudentBl::class.java.name)
    }

    //Método para crear un estudiante
    fun createStudent(studentDto: StudentDto): Long{
        StudentBl.LOGGER.info("Iniciando logica para crear un estudiante")
        //Se debe guardar en persona -> usuario -> estudiante
        //Guardando en persona y usuario
        val personaId = createPersona(studentDto)
        val userId = createUser(studentDto, personaId)
        //Creamos el registro en estudiante
        val estudiante = Student()
        estudiante.semestre = studentDto.semestre
        estudiante.tipo = "prueba" //TODO: Cambiar por el tipo de estudiante, si se mantiene
        estudiante.userId = userId
        estudiante.colegioId = studentDto.colegioId
        estudiante.estado = studentDto.estado
        val estudianteRegistrado = studentRepository.save(estudiante)
        StudentBl.LOGGER.info("Se ha guardado el registro en estudiante")
        return estudianteRegistrado.estudianteId
    }

    //Método para crear un registro en persona y retornar el id
    fun createPersona(studentDto: StudentDto): Long{
        val persona = Persona()
        persona.nombre = studentDto.nombre
        persona.apellidoPaterno = studentDto.apellidoPaterno
        persona.apellidoMaterno = studentDto.apellidoMaterno
        persona.carnetIdentidad = studentDto.carnetIdentidad
        persona.genero = studentDto.genero
        persona.correo = studentDto.correo
        persona.celular = studentDto.celular
        persona.direccion = studentDto.direccion
        persona.fechaRegistro = Date()
        persona.fechaNacimiento = studentDto.fechaNacimiento
        persona.estadoCivil = studentDto.estadoCivil
        persona.descripcion = studentDto.descripcion
        persona.uuidFoto = studentDto.uuidFoto
        persona.uuidPortada = studentDto.uuidPortada
        persona.estado = studentDto.estado
        val personaResgistrada = personaRepository.save(persona)
        StudentBl.LOGGER.info("Se ha guardado el registro en persona")
        return personaResgistrada.personaId
    }

    //Método para crear un registro en usuario y retornar el id
    fun createUser(studentDto: StudentDto, personaId: Long): Long{
        val user = User()
        user.username = studentDto.username
        user.secret = studentDto.secret
        user.personaId = personaId
        user.estado = studentDto.estado
        val usuarioRegistrado = userRepository.save(user)
        StudentBl.LOGGER.info("Se ha guardado el registro en usuario")
        return usuarioRegistrado.userId
}