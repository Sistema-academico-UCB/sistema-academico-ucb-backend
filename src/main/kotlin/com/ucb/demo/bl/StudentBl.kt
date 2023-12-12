package com.ucb.demo.bl
import at.favre.lib.crypto.bcrypt.BCrypt
import com.ucb.demo.dao.Persona
import com.ucb.demo.dao.Student
import com.ucb.demo.dao.StudentCareer
import com.ucb.demo.dao.User
import com.ucb.demo.dao.repository.*
import com.ucb.demo.dto.StudentDto
import com.ucb.demo.dto.StudentListDto
import com.ucb.demo.util.UcbException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.http.HttpStatus
import java.util.*


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
        LOGGER.info("Iniciando logica para crear un estudiante")
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
                    periodoAcademicoId = 10, //TODO: Cambiar por el periodo academico actual
                    estado = studentDto.estado
                )
            )


            LOGGER.info("Se ha guardado el registro en estudiante")
            return estudianteRegistrado.estudianteId
        }catch(e: Exception){
            LOGGER.warn("No se ha podido guardar el registro en persona y usuario")
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
            val usuarioRegistrado = userRepository.findByPersonaIdAndEstado(personaRegistrada.personaId, true)
            val registrado = studentRepository.existsByUserIdAndEstado(usuarioRegistrado.userId, true)
            if (registrado) {
                LOGGER.warn("Ya existe un estudiante con el mismo correo y CI")
                throw Exception("Ya existe un estudiante con el mismo correo y CI")
            }

        }catch(exception: EmptyResultDataAccessException){
            registroPersona = personaRepository.save(persona)
            LOGGER.info("Se ha guardado el registro en persona")
        }
        return registroPersona.personaId
    }

    //Método para crear un registro en usuario y retornar el id
    fun createUser(studentDto: StudentDto, personaId: Long): Long {
        val user = User()
        user.username = studentDto.username

        //Encriptamos la constraseña con BCrypt
        val secret: String = BCrypt.withDefaults().hashToString(12, studentDto.secret.toCharArray())
        user.rol = studentDto.rol
        user.secret = secret
        user.personaId = personaId
        user.estado = studentDto.estado
        val usuarioRegistrado = userRepository.save(user)
        LOGGER.info("Se ha guardado el registro en usuario")
        return usuarioRegistrado.userId
    }

    //Método para obtener un estudiante por su id
    fun getStudentById(studentId: Long): StudentDto {
        LOGGER.info("Iniciando logica para obtener un estudiante por su id")
        val estudiante = studentRepository.findById(studentId)
        val estudianteDto: StudentDto
        if (estudiante.isPresent && estudiante.get().estado) {
            //Obtenemos el usuario
            val usuario = userRepository.findById(estudiante.get().userId)
            //Obtenemos la persona
            val persona = personaRepository.findById(usuario.get().personaId)
            //Obtenemos el registro de la tabla intermedia - Carrera - Estudiante
            val carreraEstudiante = studentCareerRepository.findByEstudianteIdAndEstado(estudiante.get().estudianteId, true)
            if(carreraEstudiante == null){
                LOGGER.warn("No se ha encontrado la carrera del estudiante")
                throw UcbException("No se ha encontrado la carrera del estudiante")
            }
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
                    carreraId = carreraEstudiante[0].carreraId,
            )
            LOGGER.info("Se ha encontrado el estudiante")
        } else {
            LOGGER.warn("No se ha encontrado el estudiante")
            throw Exception("No se ha encontrado el estudiante")
        }
        return estudianteDto
    }

    // Método para actualizar un estudiante por su Id
    fun updateStudent(studentDto: StudentDto): String {
        LOGGER.info("Iniciando logica para actualizar un estudiante")
        // Primero, se debe obtener el registro de estudiante
        val estudianteAlmacenado: Student = studentRepository.findById(studentDto.estudianteId).orElse(null)
        estudianteAlmacenado.colegioId = studentDto.colegioId
        estudianteAlmacenado.semestre = studentDto.semestre
        val userId = studentRepository.save(estudianteAlmacenado).userId
        LOGGER.info("Se actualizo en la tabla estudiante")

        //Actualizar tablas intermedias
        LOGGER.info("Iniciando logica para actualizar un la carrera de un estudiante")
        val carreraEstudiante = studentCareerRepository.findByEstudianteIdAndEstado(estudianteAlmacenado.estudianteId, true)
        if (carreraEstudiante != null) {
            carreraEstudiante[0].carreraId = studentDto.carreraId
            studentCareerRepository.save(carreraEstudiante[0])
            LOGGER.info("Se actualizo en la tabla carrera_estudiante")
        }


        //Segundo, se debe actualizar en user
        val personaId = updateUser(studentDto, userId)

        //Tercero, se debe actualizar en persona
        updatePersona(studentDto, personaId)

        LOGGER.info("Se ha actualizado el registro del estudiante")
        return "Registro " + studentDto.estudianteId + " actualizado correctamente"
    }

    //Método para actualizar un registro en persona y retornar el id
    fun updatePersona(studentDto: StudentDto, personaId: Long){
        LOGGER.info("Iniciando logica para actualizar una persona")
        // Primero, se debe obtener el registro de la persona
        val personaAlmacenada: Persona? = personaRepository.findByPersonaIdAndEstado(personaId, true)
        if (personaAlmacenada == null) {
            LOGGER.warn("No se ha encontrado la persona")
            throw Exception("No se ha encontrado la persona")
        }
        personaAlmacenada.correo = studentDto.correo
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
        LOGGER.info("Se actualizo en la tabla persona")
    }

    //Método para actualizar un registro en usuario y retornar el id
    fun updateUser(studentDto: StudentDto, userId: Long): Long {
        LOGGER.info("Iniciando logica para actualizar un usuario")
        // Primero, se debe obtener el registro de la persona
        val usuarioAlmacenado: User = userRepository.findById(userId).orElse(null)
        usuarioAlmacenado.rol = studentDto.rol
        usuarioAlmacenado.secret = studentDto.secret
        usuarioAlmacenado.username = studentDto.username
        val registroAlmacenado = userRepository.save(usuarioAlmacenado)
        LOGGER.info("Se actualizo en la tabla usuario")
        return registroAlmacenado.personaId
    }


    //Metodo para obtener todos los estudiantes
    fun getAllStudents(page: Int,
                       size: Int,
                       carnetIdentidad: String?,
                       semestre: Int?,
                       carreraId: Long?,
                       nombre: String?,
                       sortBy: String,
                       sortType: String ): List<Any> {
        LOGGER.info("Iniciando logica para obtener todos los estudiantes")
        val pageable: Pageable = PageRequest.of(page, size)
        //Lista de estudiantes
        //val list: List<Student> = pagingRepository.findAllByEstado(true, pageable).toList()
        print(carreraId)
        val pageStudents: Page<Student> = studentRepository.filtrarEstudiantes(carnetIdentidad, semestre, carreraId, nombre, sortBy, sortType, pageable)
        val list: List<Student> = pageStudents.content
        //Obtener usuarios por id, de la lista
        print(list)
        val users: MutableList<User> = mutableListOf()
        for(student in list){
            //Obtenemos el usuario
            val usuario = userRepository.findByUserIdAndEstado(student.userId, true)
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

        //Obtener carreras por id, de la lista
        val carreras: MutableList<StudentCareer> = mutableListOf()
        for(student in list){
            //Obtenemos la carrera
            val carrera = studentCareerRepository.findByEstudianteIdAndEstado(student.estudianteId, true)
            if(carrera!=null){
                carreras.add(carrera[0])
            }
        }

        //Creamos la lista de estudiantes
        val estudiantes: MutableList<StudentListDto> = mutableListOf()

        for(i in 0 until users.size){
            estudiantes.add(StudentListDto(
                    userId = users[i].userId,
                    estudianteId = list[i].estudianteId,
                    semestre = list[i].semestre,
                    colegioId = list[i].colegioId,
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
                    carreraId = carreras[i].carreraId,
            ))
        }

        LOGGER.info("Se ha obtenido la lista de estudiantes")
        return listOf(estudiantes,pageStudents.totalElements)
    }
}
