package com.ucb.demo.bl

import com.ucb.demo.dao.Teacher
import com.ucb.demo.dao.Persona
import com.ucb.demo.dao.User
import com.ucb.demo.dao.userRepository.TeacherRepository
import com.ucb.demo.dao.userRepository.PersonaRepository
import com.ucb.demo.dao.userRepository.UserRepository
import com.ucb.demo.dto.TeacherDto
import com.ucb.demo.dto.ResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Date

@Service
class TeacherBl @Autowired constructor(
    private val teacherRepository: TeacherRepository,
    private val personaRepository: PersonaRepository,
    private val userRepository: UserRepository
){
    //Logger
    companion object{
        val LOGGER = org.slf4j.LoggerFactory.getLogger(TeacherBl::class.java)
    }

    //Método para crear un docente
    fun createTeacher(teacherDto: TeacherDto):Long{
        TeacherBl.LOGGER.info("Iniciando logica para crear un docente")
        //Se debe guardar en persona -> usuario -> docente
        //Guardando en persona y usuario
        val personaId = createPersona(teacherDto)
        val userId = createUser(teacherDto, personaId)
        //Creamos el registro en docente
        val docente = Teacher()
        docente.tipo = teacherDto.tipo
        docente.userId = userId
        docente.estado = teacherDto.estado
        val registroDocente = teacherRepository.save(docente)
        TeacherBl.LOGGER.info("Se ha guardado el registro en docente")
        return registroDocente.docenteId
    }

    //Método para crear un registro en persona y retornar el id
    fun createPersona(teacherDto: TeacherDto): Long{
        val persona = Persona()
        persona.nombre = teacherDto.nombre
        persona.apellidoPaterno = teacherDto.apellidoPaterno
        persona.apellidoMaterno = teacherDto.apellidoMaterno
        persona.carnetIdentidad = teacherDto.carnetIdentidad
        persona.genero = teacherDto.genero
        persona.correo = teacherDto.correo
        persona.celular = teacherDto.celular
        persona.direccion = teacherDto.direccion
        persona.fechaRegistro = Date()
        persona.fechaNacimiento = teacherDto.fechaNacimiento
        persona.estadoCivil = teacherDto.estadoCivil
        persona.descripcion = teacherDto.descripcion
        persona.uuidFoto = teacherDto.uuidFoto
        persona.uuidPortada = teacherDto.uuidPortada
        persona.estado = teacherDto.estado
        val registroPersona = personaRepository.save(persona)
        TeacherBl.LOGGER.info("Se ha guardado el registro en persona")
        return registroPersona.personaId
    }

    //Método para crear un registro en usuario y retornar el id
    fun createUser(teacherDto: TeacherDto, personaId: Long): Long{
        val user = User()
        user.username = teacherDto.username
        user.secret = teacherDto.secret //TODO: Encriptar
        user.rol = teacherDto.rol
        user.personaId = personaId
        user.estado = teacherDto.estado
        val registroUsuario = userRepository.save(user)
        TeacherBl.LOGGER.info("Se ha guardado el registro en usuario")
        return registroUsuario.userId
    }
}