package com.ucb.demo.bl

import com.ucb.demo.dao.*
import com.ucb.demo.dao.repository.*
import com.ucb.demo.dto.TeacherDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import at.favre.lib.crypto.bcrypt.BCrypt
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import java.util.*

//TODO: Conexion con tablas intermediarias
@Service
class TeacherBl @Autowired constructor(
    private val teacherRepository: TeacherRepository,
    private val personaRepository: PersonaRepository,
    private val userRepository: UserRepository,
    private val teacherProfessionRepository: TeacherProfessionRepository,
    private val teacherDepartmentRepository: TeacherDepartmentRepository
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
        try{
            val personaId = createPersona(teacherDto)
            val userId = createUser(teacherDto, personaId)
            //Creamos el registro en docente
            val docente = Teacher()
            docente.tipo = teacherDto.tipo
            docente.userId = userId
            docente.estado = teacherDto.estado
            
            val registroDocente = teacherRepository.save(docente)
            TeacherBl.LOGGER.info("Se ha guardado el registro en docente")
            
            //Guardar la informacion en la tabla intermedia - docente_profession
            teacherProfessionRepository.save(
                TeacherProfession(
                profesionId = teacherDto.profesionId,
                docenteId = registroDocente.docenteId
            ))

            //Guardar la informacion en la tabla intermedia - docente_departamento_carrera
            teacherDepartmentRepository.save(
                TeacherDepartment(
                    departamentoCarreraId = teacherDto.departamentoCarreraId,
                    docenteId = registroDocente.docenteId,
                    directorCarrera = teacherDto.directorCarrera,
                    estado = teacherDto.estado
                ))

            return registroDocente.docenteId
        }catch(e: Exception){
            TeacherBl.LOGGER.warn("No se ha podido guardar el registro en persona y usuario")
            TeacherBl.LOGGER.error(e.message)
            return HttpStatus.INTERNAL_SERVER_ERROR.value().toLong()
        }    
    }

    //Método para crear un registro en persona y retornar el id
    fun createPersona(teacherDto: TeacherDto): Long{
        val persona = Persona()
        var registroPersona = Persona()
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

        try {
            //Verificamos que no exista una persona con el mismo correo y ci
            val personaRegistrada = personaRepository.findByCorreoAndCarnetIdentidadAndEstado(teacherDto.correo, teacherDto.carnetIdentidad, true)
            //En el caso de que exista una persona con el mismo correo, verificamos que no sea un docente
            var usuarioRegistrado = userRepository.findByPersonaIdAndEstado(personaRegistrada.personaId, true)
            var registrado = teacherRepository.existsByUserIdAndEstado(usuarioRegistrado.userId, true)
            if (registrado) {
                TeacherBl.LOGGER.warn("Ya existe un docente con el mismo correo y CI")
                throw Exception("Ya existe un docente con el mismo correo y CI")
            }

        }catch(exception: EmptyResultDataAccessException){
            registroPersona = personaRepository.save(persona)
            TeacherBl.LOGGER.info("Se ha guardado el registro en persona")
        }
        return registroPersona.personaId
    }

    //Método para crear un registro en usuario y retornar el id
    fun createUser(teacherDto: TeacherDto, personaId: Long): Long{
        val user = User()
        user.username = teacherDto.username

        //Encriptamos la constraseña con BCrypt
        val secret: String = BCrypt.withDefaults().hashToString(12, teacherDto.secret.toCharArray())

        user.secret = secret
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
            TeacherBl.LOGGER.warn("No se ha encontrado el profesor")
            throw Exception("No se ha encontrado el profesor")
        }
        return profesorDto
    }

    // Método para actualizar un estudiante por su Id
    fun updateTeacher(teacherDto: TeacherDto): String {
        TeacherBl.LOGGER.info("Iniciando logica para actualizar un docente")
        // Primero, se debe obtener el registro de estudiante
        val docenteAlmacenado: Teacher = teacherRepository.findById(teacherDto.docenteId).orElse(null)
        docenteAlmacenado.tipo = teacherDto.tipo
        val userId = teacherRepository.save(docenteAlmacenado).userId
        StudentBl.LOGGER.info("Se actualizo en la tabla docente")

        //TODO: Subtarea - actualizar tablas intermedias
        //TeacherBl.LOGGER.info("Iniciando logica para actualizar la profesion de un docente")
        //TeacherBl.LOGGER.info("Iniciando logica para actualizar el departamento de un docente")

        //Segundo, se debe actualizar en user
        val personaId = updateUser(teacherDto, userId)

        //Tercero, se debe actualizar en persona
        updatePersona(teacherDto, personaId)

        StudentBl.LOGGER.info("Se ha actualizado el registro del docente")
        return "Registro " + teacherDto.docenteId + " actualizado correctamente"
    }

    //Método para actualizar un registro en persona y retornar el id
    fun updatePersona(teacherDto: TeacherDto, personaId: Long){
        TeacherBl.LOGGER.info("Iniciando logica para actualizar una persona")
        // Primero, se debe obtener el registro de la persona
        val personaAlmacenada: Persona = personaRepository.findById(personaId).orElse(null)
        //personaAlmacenada.correo = teacherDto.correo
        personaAlmacenada.celular = teacherDto.celular
        personaAlmacenada.apellidoMaterno = teacherDto.apellidoMaterno
        personaAlmacenada.apellidoPaterno = teacherDto.apellidoPaterno
        personaAlmacenada.carnetIdentidad = teacherDto.carnetIdentidad
        personaAlmacenada.descripcion = teacherDto.descripcion
        personaAlmacenada.direccion = teacherDto.direccion
        personaAlmacenada.estadoCivil = teacherDto.estadoCivil
        personaAlmacenada.fechaNacimiento = teacherDto.fechaNacimiento
        personaAlmacenada.genero = teacherDto.genero
        personaAlmacenada.nombre = teacherDto.nombre
        personaAlmacenada.uuidFoto = teacherDto.uuidFoto
        personaAlmacenada.uuidPortada = teacherDto.uuidPortada
        personaRepository.save(personaAlmacenada)
        TeacherBl.LOGGER.info("Se actualizo en la tabla persona")
    }

    //Método para actualizar un registro en usuario y retornar el id
    fun updateUser(teacherDto: TeacherDto, userId: Long): Long {
        TeacherBl.LOGGER.info("Iniciando logica para actualizar un usuario")
        // Primero, se debe obtener el registro de la persona
        val usuarioAlmacenado: User = userRepository.findById(userId).orElse(null)
        usuarioAlmacenado.rol = teacherDto.rol
        usuarioAlmacenado.secret = teacherDto.secret
        usuarioAlmacenado.username = teacherDto.username
        val registroAlmacenado = userRepository.save(usuarioAlmacenado)
        StudentBl.LOGGER.info("Se actualizo en la tabla usuario")
        return registroAlmacenado.personaId
    }
}