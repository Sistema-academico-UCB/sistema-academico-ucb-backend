package com.ucb.demo.bl
import at.favre.lib.crypto.bcrypt.BCrypt
import com.ucb.demo.dao.Persona
import com.ucb.demo.dao.Student
import com.ucb.demo.dao.StudentCareer
import com.ucb.demo.dao.User
import com.ucb.demo.dao.repository.PersonaRepository
import com.ucb.demo.dao.repository.StudentCareerRepository
import com.ucb.demo.dao.repository.StudentRepository
import com.ucb.demo.dao.repository.UserRepository
import com.ucb.demo.dto.StudentDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.http.HttpStatus
import java.util.*

//TODO: Conexion con tablas intermediarias
@Service
class StudentBl @Autowired constructor(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val personaRepository: PersonaRepository,
    private val studentCareerRepository: StudentCareerRepository,

) {
    // Logger
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(StudentBl::class.java.name)
    }

    //Método para crear un estudiante
    fun createStudent(studentDto: StudentDto): Long {
        StudentBl.LOGGER.info("Iniciando logica para crear un estudiante")
        //Se debe guardar en persona -> usuario -> estudiante
        //Guardando en persona y usuario
        try{
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
            //Guardar registro en tabla intermedia - Carrera - Estudiante
            studentCareerRepository.save(
                StudentCareer(
                    estudianteId = estudianteRegistrado.estudianteId,
                    carreraId = studentDto.carreraId,
                    periodoAcademicoId = 10,
                    estado = studentDto.estado
                )
            )
            StudentBl.LOGGER.info("Se ha guardado el registro en estudiante")
            return estudianteRegistrado.estudianteId
        }catch(e: Exception){
            StudentBl.LOGGER.warn("No se ha podido guardar el registro en persona y usuario")
            return HttpStatus.INTERNAL_SERVER_ERROR.value().toLong()
        }
        
    }

    //Método para crear un registro en persona y retornar el id
    fun createPersona(studentDto: StudentDto): Long {
        val persona = Persona()
        var registroPersona = Persona()
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

        try {
            //Verificamos que no exista una persona con el mismo correo y ci
            val personaRegistrada = personaRepository.findByCorreoAndCarnetIdentidadAndEstado(studentDto.correo, studentDto.carnetIdentidad, true)
            //En el caso de que exista una persona con el mismo correo, verificamos que no sea un docente
            var usuarioRegistrado = userRepository.findByPersonaIdAndEstado(personaRegistrada.personaId, true)
            var registrado = studentRepository.existsByUserIdAndEstado(usuarioRegistrado.userId, true)
            if (registrado) {
                TeacherBl.LOGGER.warn("Ya existe un estudiante con el mismo correo y CI")
                throw Exception("Ya existe un estudiante con el mismo correo y CI")
            }

        }catch(exception: EmptyResultDataAccessException){
            registroPersona = personaRepository.save(persona)
            TeacherBl.LOGGER.info("Se ha guardado el registro en persona")
        }
        return registroPersona.personaId
    }

    //Método para crear un registro en usuario y retornar el id
    fun createUser(studentDto: StudentDto, personaId: Long): Long {
        val user = User()
        user.username = studentDto.username

        //Encriptamos la constraseña con BCrypt
        val secret: String = BCrypt.withDefaults().hashToString(12, studentDto.secret.toCharArray())


        user.secret = secret
        user.personaId = personaId
        user.estado = studentDto.estado
        val usuarioRegistrado = userRepository.save(user)
        StudentBl.LOGGER.info("Se ha guardado el registro en usuario")
        return usuarioRegistrado.userId
    }

    //Método para obtener un estudiante por su id
    fun getStudentById(studentId: Long): StudentDto {
        StudentBl.LOGGER.info("Iniciando logica para obtener un estudiante por su id")
        val estudiante = studentRepository.findById(studentId)
        var estudianteDto: StudentDto
        if (estudiante.isPresent && estudiante.get().estado) {
            //Obtenemos el usuario
            val usuario = userRepository.findById(estudiante.get().userId)
            //Obtenemos la persona
            val persona = personaRepository.findById(usuario.get().personaId)
            //Creamos el objeto de respuesta
            estudianteDto = StudentDto(
                    estudianteId = estudiante.get().estudianteId,
                    semestre = estudiante.get().semestre,
                    colegioId = estudiante.get().colegioId,
                    estado = estudiante.get().estado,
                    username = usuario.get().username,
                    secret = usuario.get().secret,
                    nombre = persona.get().nombre,
                    apellidoPaterno = persona.get().apellidoPaterno,
                    apellidoMaterno = persona.get().apellidoMaterno,
                    carnetIdentidad = persona.get().carnetIdentidad,
                    genero = persona.get().genero,
                    correo = persona.get().correo,
                    celular = persona.get().celular,
                    direccion = persona.get().direccion,
                    fechaRegistro = persona.get().fechaRegistro,
                    fechaNacimiento = persona.get().fechaNacimiento,
                    estadoCivil = persona.get().estadoCivil,
                    descripcion = persona.get().descripcion,
                    uuidFoto = persona.get().uuidFoto,
                    uuidPortada = persona.get().uuidPortada,
                    rol = usuario.get().rol,
                    carreraId = 1, //TODO: Cambiar por el id de la carrera
            )
        } else {
            StudentBl.LOGGER.warn("No se ha encontrado el estudiante")
            throw Exception("No se ha encontrado el estudiante")
        }
        return estudianteDto
    }

    // Método para actualizar un estudiante por su Id
    fun updateStudent(studentDto: StudentDto): String {
        StudentBl.LOGGER.info("Iniciando logica para actualizar un estudiante")
        // Primero, se debe obtener el registro de estudiante
        val estudianteAlmacenado: Student = studentRepository.findById(studentDto.estudianteId).orElse(null)
        estudianteAlmacenado.colegioId = studentDto.colegioId
        estudianteAlmacenado.semestre = studentDto.semestre
        val userId = studentRepository.save(estudianteAlmacenado).userId
        StudentBl.LOGGER.info("Se actualizo en la tabla estudiante")

        //TODO: Subtarea - actualizar tablas intermedias
        //StudentBl.LOGGER.info("Iniciando logica para actualizar un la carrera de un estudiante")

        //Segundo, se debe actualizar en user
        val personaId = updateUser(studentDto, userId)

        //Tercero, se debe actualizar en persona
        updatePersona(studentDto, personaId)

        StudentBl.LOGGER.info("Se ha actualizado el registro del estudiante")
        return "Registro " + studentDto.estudianteId + " actualizado correctamente"
    }

    //Método para actualizar un registro en persona y retornar el id
    fun updatePersona(studentDto: StudentDto, personaId: Long){
        StudentBl.LOGGER.info("Iniciando logica para actualizar una persona")
        // Primero, se debe obtener el registro de la persona
        val personaAlmacenada: Persona = personaRepository.findById(personaId).orElse(null)
        //personaAlmacenada.correo = studentDto.correo
        personaAlmacenada.celular = studentDto.celular
        personaAlmacenada.apellidoMaterno = studentDto.apellidoMaterno
        personaAlmacenada.apellidoPaterno = studentDto.apellidoPaterno
        personaAlmacenada.carnetIdentidad = studentDto.carnetIdentidad
        personaAlmacenada.descripcion = studentDto.descripcion
        personaAlmacenada.direccion = studentDto.direccion
        personaAlmacenada.estadoCivil = studentDto.estadoCivil
        personaAlmacenada.fechaNacimiento = studentDto.fechaNacimiento
        personaAlmacenada.genero = studentDto.genero
        personaAlmacenada.nombre = studentDto.nombre
        personaAlmacenada.uuidFoto = studentDto.uuidFoto
        personaAlmacenada.uuidPortada = studentDto.uuidPortada
        personaRepository.save(personaAlmacenada)
        StudentBl.LOGGER.info("Se actualizo en la tabla persona")
    }

    //Método para actualizar un registro en usuario y retornar el id
    fun updateUser(studentDto: StudentDto, userId: Long): Long {
        StudentBl.LOGGER.info("Iniciando logica para actualizar un usuario")
        // Primero, se debe obtener el registro de la persona
        val usuarioAlmacenado: User = userRepository.findById(userId).orElse(null)
        usuarioAlmacenado.rol = studentDto.rol
        usuarioAlmacenado.secret = studentDto.secret
        usuarioAlmacenado.username = studentDto.username
        val registroAlmacenado = userRepository.save(usuarioAlmacenado)
        StudentBl.LOGGER.info("Se actualizo en la tabla usuario")
        return registroAlmacenado.personaId
    }
}
