package com.ucb.demo.bl
import com.ucb.demo.dao.Persona
import com.ucb.demo.dao.Student
import com.ucb.demo.dao.User
import com.ucb.demo.dao.repository.PersonaRepository
import com.ucb.demo.dao.repository.StudentRepository
import com.ucb.demo.dao.repository.UserRepository
import com.ucb.demo.dto.StudentDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

//TODO: Conexion con tablas intermediarias
@Service
class StudentBl @Autowired constructor(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val personaRepository: PersonaRepository

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
    fun createPersona(studentDto: StudentDto): Long {
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
    fun createUser(studentDto: StudentDto, personaId: Long): Long {
        val user = User()
        user.username = studentDto.username
        user.secret = studentDto.secret
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

    // Método para actualizar un estudiante - en progreso
    /*
    fun updateStudent(studentId: Long,studentDto: StudentDto): StudentDto {
        StudentBl.LOGGER.info("Iniciando logica para actualizar un estudiante")
        // Primero, obtén el usuario que deseas actualizar
        val usuario: User = userRepository.findById(id).orElse(null)
        //Se debe actualizar en persona -> usuario -> estudiante
        //Actualizando en persona
        val personaId = updatePersona(studentDto)
        //Actualizando en usuario
        val userId = updateUser(studentDto, personaId)
        //Actualizando en estudiante
        val estudiante = Student()
        estudiante.estudianteId = studentDto.estudianteId
        estudiante.semestre = studentDto.semestre
        estudiante.tipo = "prueba" //TODO: Cambiar por el tipo de estudiante, si se mantiene
        estudiante.userId = userId
        estudiante.colegioId = studentDto.colegioId
        estudiante.estado = studentDto.estado
        val estudianteActualizado = studentRepository.save(estudiante)
        StudentBl.LOGGER.info("Se ha actualizado el registro en estudiante")
        return studentDto
    }

    //Método para actualizar un registro en persona y retornar el id
    fun updatePersona(studentDto: StudentDto): Long {
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
        val personaActualizada = personaRepository.save(persona)
        StudentBl.LOGGER.info("Se ha actualizado el registro en persona")
        return personaActualizada.personaId
    }

    //Método para actualizar un registro en usuario y retornar el id
    fun updateUser(studentDto: StudentDto, personaId: Long): Long {
        val user = User()
        user.username = studentDto.username
        user.secret = studentDto.secret
        user.personaId = personaId
        user.estado = studentDto.estado
        val usuarioActualizado = userRepository.save(user)
        StudentBl.LOGGER.info("Se ha actualizado el registro en usuario")
        return usuarioActualizado.userId
    }*/
}
