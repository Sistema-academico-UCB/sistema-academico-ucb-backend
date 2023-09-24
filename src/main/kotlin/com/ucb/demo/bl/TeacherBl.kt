package com.ucb.demo.bl

import com.ucb.demo.dao.Teacher
import com.ucb.demo.dao.Persona
import com.ucb.demo.dao.User
import com.ucb.demo.dao.repository.TeacherRepository
import com.ucb.demo.dao.repository.PersonaRepository
import com.ucb.demo.dao.repository.UserRepository
import com.ucb.demo.dto.TeacherDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Date

//TODO: Conexion con tablas intermediarias
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

    //Método para obtener un profesor por su id
    fun getTeacherById(teacherId: Long): TeacherDto{
        TeacherBl.LOGGER.info("Iniciando logica para obtener un profesor por su id")
        val profesor = teacherRepository.findById(teacherId)
        var profesorDto: TeacherDto
        if (profesor.isPresent && profesor.get().estado) {
            //Obtenemos el usuario
            val usuario = userRepository.findById(profesor.get().userId)
            //Obtenemos la persona
            val persona = personaRepository.findById(usuario.get().personaId)
            //Creamos el objeto de respuesta
            profesorDto = TeacherDto(
                docenteId = profesor.get().docenteId,
                nombre = persona.get().nombre,
                apellidoPaterno = persona.get().apellidoPaterno,
                apellidoMaterno = persona.get().apellidoMaterno,
                carnetIdentidad = persona.get().carnetIdentidad,
                fechaNacimiento = persona.get().fechaNacimiento,
                correo = persona.get().correo,
                genero = persona.get().genero,
                celular = persona.get().celular,
                descripcion = persona.get().descripcion,
                uuidFoto = persona.get().uuidFoto,
                uuidPortada = persona.get().uuidPortada,
                direccion = persona.get().direccion,
                fechaRegistro = persona.get().fechaRegistro,
                estadoCivil = persona.get().estadoCivil,
                username = usuario.get().username,
                secret = usuario.get().secret,
                rol = usuario.get().rol,
                tipo = profesor.get().tipo,
                profesionId = 1, //TODO: Cambiar por el id de la profesion
                departamentoCarreraId = 1, //TODO: Cambiar por el id del departamento
                directorCarrera = true, //TODO: Cambiar por el valor de la tabla intermedia
                estado = profesor.get().estado
            )
            TeacherBl.LOGGER.info("Se ha encontrado el profesor")
        } else {
            StudentBl.LOGGER.warn("No se ha encontrado el profesor")
            throw Exception("No se ha encontrado el profesor")
        }
        return profesorDto
    }
}