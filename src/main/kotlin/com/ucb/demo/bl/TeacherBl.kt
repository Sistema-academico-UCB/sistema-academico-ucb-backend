package com.ucb.demo.bl

import com.ucb.demo.dao.*
import com.ucb.demo.dao.repository.*
import com.ucb.demo.dto.TeacherDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import at.favre.lib.crypto.bcrypt.BCrypt
import com.ucb.demo.dto.StudentDto
import com.ucb.demo.dto.TeacherListDto
import org.slf4j.Logger
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*


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
        val LOGGER: Logger = org.slf4j.LoggerFactory.getLogger(TeacherBl::class.java)
    }

    //Método para crear un docente
    fun createTeacher(teacherDto: TeacherDto):Long{
        LOGGER.info("Iniciando logica para crear un docente")
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
            LOGGER.info("Se ha guardado el registro en docente")
            
            //Guardar la informacion en la tabla intermedia - docente_profession
            teacherProfessionRepository.save(
                TeacherProfession(
                profesionId = teacherDto.profesionId,
                docenteId = registroDocente.docenteId,
                estado = teacherDto.estado
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
            LOGGER.warn("No se ha podido guardar el registro en persona y usuario")
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
            val usuarioRegistrado = userRepository.findByPersonaIdAndEstado(personaRegistrada.personaId, true)
            val registrado = teacherRepository.existsByUserIdAndEstado(usuarioRegistrado.userId, true)
            if (registrado) {
                LOGGER.warn("Ya existe un docente con el mismo correo y CI")
                throw Exception("Ya existe un docente con el mismo correo y CI")
            }

        }catch(exception: EmptyResultDataAccessException){
            registroPersona = personaRepository.save(persona)
            LOGGER.info("Se ha guardado el registro en persona")
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
        LOGGER.info("Se ha guardado el registro en usuario")
        return registroUsuario.userId
    }

    //Método para obtener un profesor por su id
    fun getTeacherById(teacherId: Long): TeacherDto{
        LOGGER.info("Iniciando logica para obtener un profesor por su id")
        val profesor = teacherRepository.findById(teacherId)
        val profesorDto: TeacherDto
        if (profesor.isPresent && profesor.get().estado) {
            //Obtenemos el usuario
            val usuario = userRepository.findById(profesor.get().userId)
            //Obtenemos la persona
            val persona = personaRepository.findById(usuario.get().personaId)
            //Obtenemos la profesion
            val profesion: TeacherProfession? = teacherProfessionRepository.findByDocenteIdAndEstado(teacherId, true)
            val profesionId: Long = profesion?.profesionId ?: 0
            //Obtenemos el departamento
            val departamento: TeacherDepartment? = teacherDepartmentRepository.findByDocenteIdAndEstadoTrue(teacherId)
            val departamentoId: Long = departamento?.departamentoCarreraId ?: 0
            val directorCarrera: Boolean = departamento?.directorCarrera ?: false
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
                profesionId = profesionId,
                departamentoCarreraId = departamentoId,
                directorCarrera = directorCarrera,
                estado = profesor.get().estado
            )
            LOGGER.info("Se ha encontrado el profesor")
        } else {
            LOGGER.warn("No se ha encontrado el profesor")
            throw Exception("No se ha encontrado el profesor")
        }
        return profesorDto
    }

    // Método para actualizar un docente por su Id
    fun updateTeacher(teacherDto: TeacherDto): String {
        LOGGER.info("Iniciando logica para actualizar un docente")
        // Primero, se debe obtener el registro del docente
        val docenteAlmacenado: Teacher = teacherRepository.findById(teacherDto.docenteId).orElse(null)
        docenteAlmacenado.tipo = teacherDto.tipo
        val userId = teacherRepository.save(docenteAlmacenado).userId
        LOGGER.info("Se actualizo en la tabla docente")

        //Actualizar tablas intermedias
        TeacherBl.LOGGER.info("Iniciando logica para actualizar la profesion de un docente")
        val profesion: TeacherProfession? = teacherProfessionRepository.findByDocenteIdAndEstado(teacherDto.docenteId, true)
        val profesionId: Long = profesion?.profesionId ?: 0
        if(profesionId != 0.toLong()) {
            if (profesionId == teacherDto.profesionId) {
                TeacherBl.LOGGER.info("No se ha actualizado la profesion del docente")
            } else {
                profesion!!.estado = false
                teacherProfessionRepository!!.save(profesion)
                teacherProfessionRepository.save(
                    TeacherProfession(
                        profesionId = teacherDto.profesionId,
                        docenteId = teacherDto.docenteId,
                        estado = true
                    )
                )
                TeacherBl.LOGGER.info("Se ha actualizado la profesion del docente")
            }
        } else {
            TeacherBl.LOGGER.info("No se ha actualizado la profesion del docente")
        }
        TeacherBl.LOGGER.info("Iniciando logica para actualizar el departamento de un docente")
        val departamento: TeacherDepartment? = teacherDepartmentRepository.findByDocenteIdAndEstadoTrue(teacherDto.docenteId)
        val departamentoId: Long = departamento?.departamentoCarreraId ?: 0
        if(departamentoId != 0.toLong()) {
            val directorCarrera: Boolean = departamento?.directorCarrera ?: false
            if (departamentoId == teacherDto.departamentoCarreraId) {
                TeacherBl.LOGGER.info("No se ha actualizado el departamento del docente")
                if (directorCarrera == teacherDto.directorCarrera) {
                    TeacherBl.LOGGER.info("No se ha actualizado el director de carrera del docente")
                } else {
                    departamento!!.directorCarrera = teacherDto.directorCarrera
                    teacherDepartmentRepository!!.save(departamento)
                    TeacherBl.LOGGER.info("Se ha actualizado el director de carrera del docente")
                }
            } else {
                departamento!!.estado = false
                teacherDepartmentRepository!!.save(departamento)
                teacherDepartmentRepository.save(
                    TeacherDepartment(
                        departamentoCarreraId = teacherDto.departamentoCarreraId,
                        docenteId = teacherDto.docenteId,
                        directorCarrera = teacherDto.directorCarrera,
                        estado = true
                    )
                )
                TeacherBl.LOGGER.info("Se ha actualizado el departamento del docente")
            }
        } else {
            TeacherBl.LOGGER.info("No se ha actualizado el departamento del docente")
        }

        //Segundo, se debe actualizar en user
        val personaId = updateUser(teacherDto, userId)

        //Tercero, se debe actualizar en persona
        updatePersona(teacherDto, personaId)

        LOGGER.info("Se ha actualizado el registro del docente")
        return "Registro " + teacherDto.docenteId + " actualizado correctamente"
    }

    //Método para actualizar un registro en persona y retornar el id
    fun updatePersona(teacherDto: TeacherDto, personaId: Long){
        LOGGER.info("Iniciando logica para actualizar una persona")
        // Primero, se debe obtener el registro de la persona
        val personaAlmacenada: Persona = personaRepository.findById(personaId).orElse(null)
        personaAlmacenada.correo = teacherDto.correo
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
        LOGGER.info("Se actualizo en la tabla persona")
    }

    //Método para actualizar un registro en usuario y retornar el id
    fun updateUser(teacherDto: TeacherDto, userId: Long): Long {
        LOGGER.info("Iniciando logica para actualizar un usuario")
        // Primero, se debe obtener el registro de la persona
        val usuarioAlmacenado: User = userRepository.findById(userId).orElse(null)
        usuarioAlmacenado.rol = teacherDto.rol
        usuarioAlmacenado.secret = teacherDto.secret
        usuarioAlmacenado.username = teacherDto.username
        val registroAlmacenado = userRepository.save(usuarioAlmacenado)
        LOGGER.info("Se actualizo en la tabla usuario")
        return registroAlmacenado.personaId
    }

    //Metodo para obtener todos los docentes
    fun getAllTeachers(page: Int,
                       size: Int,
                       carnetIdentidad: String?,
                       departamentoId: Long?,
                       nombre: String?,
                       sortBy: String,
                       sortType: String ): List<Any>{
        LOGGER.info("Iniciando logica para obtener todos los docentes")
        val pageable: Pageable = PageRequest.of(page, size)
        //Lista de docentes
        val pageDocentes = teacherRepository.filtrarDocentes(carnetIdentidad, departamentoId, nombre, sortBy, sortType, pageable)
        val list: List<Teacher> = pageDocentes.content
        
        //Obtener usuarios por id, de la lista
        val users: MutableList<User> = mutableListOf()
        for(teacher in list){
            //Obtenemos el usuario
            val usuario = userRepository.findByUserIdAndEstado(teacher.userId, true)
            if(usuario !=null){
                users.add(usuario)
            }
        }


        //Obtener personas por id, de la lista
        val personas: MutableList<Persona> = mutableListOf()
        for(user in users){
            //Obtenemos la persona
            val persona = personaRepository.findByPersonaIdAndEstado(user.personaId, true)
            if(persona!=null){
                personas.add(persona)
            }
        }


        //Obtener departamentos por id, de la lista
        val departamentos: MutableList<TeacherDepartment> = mutableListOf()
        for(teacher in list){
            //Obtenemos el departamento
            val departamento = teacherDepartmentRepository.findByDocenteIdAndEstadoTrue(teacher.docenteId)
            if(departamento!=null){
                departamentos.add(departamento)
            }
        }

        //Obtener profesiones por id, de la lista
        val profesiones: MutableList<TeacherProfession> = mutableListOf()
        for(teacher in list){
            //Obtenemos la profesion
            val profesion = teacherProfessionRepository.findByDocenteIdAndEstado(teacher.docenteId, true)
            if(profesion!=null){
                profesiones.add(profesion)
            }
        }

        //Creamos la lista de docentes
        val docentes: MutableList<TeacherListDto> = mutableListOf()

        for(i in 0 until users.size){
            docentes.add(
                TeacherListDto(
                    userId = users[i].userId,
                    docenteId = list[i].docenteId,
                    estado = list[i].estado,
                    username = users[i].username,
                    nombre = personas[i].nombre,
                    apellidoPaterno = personas[i].apellidoPaterno,
                    apellidoMaterno = personas[i].apellidoMaterno,
                    carnetIdentidad = personas[i].carnetIdentidad,
                    genero = personas[i].genero,
                    correo = personas[i].correo,
                    celular = personas[i].celular,
                    direccion = personas[i].direccion,
                    fechaRegistro = personas[i].fechaRegistro,
                    fechaNacimiento = personas[i].fechaNacimiento,
                    estadoCivil = personas[i].estadoCivil,
                    descripcion = personas[i].descripcion,
                    uuidFoto = personas[i].uuidFoto,
                    uuidPortada = personas[i].uuidPortada,
                    rol = users[i].rol,
                    departamentoCarreraId = departamentos[i].departamentoCarreraId,
                    tipo = list[i].tipo,
                    directorCarrera = departamentos[i].directorCarrera,
                    profesionId = profesiones[i].profesionId
                )
            )
        }

        LOGGER.info("Se ha obtenido la lista de docentes")
        return listOf(docentes, pageDocentes.totalElements)
    }
}