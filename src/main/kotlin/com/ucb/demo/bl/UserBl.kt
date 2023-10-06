package com.ucb.demo.bl

import com.ucb.demo.dao.repository.PersonaRepository
import com.ucb.demo.dao.repository.StudentRepository
import com.ucb.demo.dao.repository.TeacherRepository
import com.ucb.demo.dao.repository.UserRepository
import com.ucb.demo.dto.StudentAuxDto
import com.ucb.demo.dto.TeacherAuxDto
import com.ucb.demo.dto.UserDto
import com.ucb.demo.util.UcbException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserBl @Autowired constructor(
    private val userRepository: UserRepository,
        private val studentRepository: StudentRepository,
        private val teacherRepository: TeacherRepository,
        private val personaRepository: PersonaRepository
) {

    //Logger
    companion object {
        private val LOGGER = org.slf4j.LoggerFactory.getLogger(UserBl::class.java)
    }



    /**
     * Método para obtener un usuario por su id
     * @param userId
     * @return UserDto
     */
    fun getUserById(userId: Long): UserDto? {
        LOGGER.info("Iniciando logica para obtener un usuario por su id")
        var userDto: UserDto? = null
        val userEntity = userRepository.findByUserIdAndEstado(userId, true)
        if (userEntity != null) {
            val personaEntity = personaRepository.findByPersonaIdAndEstado(userEntity.personaId, true)
            // Comprobar si es un estudiante o un profesor
            if (userEntity.rol.uppercase() == "ESTUDIANTE") {
                // El usuario es un estudiante
                // Obtenemos la informacion del estudiante por su userId
                val studentEntity = studentRepository.findByUserIdAndEstado(userId, true)
                userDto = StudentAuxDto(
                        userId = userEntity.userId,
                        username = userEntity.username,
                        secret = userEntity.secret,
                        rol = userEntity.rol,
                        personaId = userEntity.personaId,
                        nombre = personaEntity.nombre,
                        apellidoPaterno = personaEntity.apellidoPaterno,
                        apellidoMaterno = personaEntity.apellidoMaterno,
                        carnetIdentidad = personaEntity.carnetIdentidad,
                        fechaNacimiento = personaEntity.fechaNacimiento,
                        correo = personaEntity.correo,
                        genero = personaEntity.genero,
                        celular = personaEntity.celular,
                        descripcion = personaEntity.descripcion,
                        uuidFoto = personaEntity.uuidFoto,
                        uuidPortada = personaEntity.uuidPortada,
                        direccion = personaEntity.direccion,
                        fechaRegistro = personaEntity.fechaRegistro,
                        estadoCivil = personaEntity.estadoCivil,
                        estado = personaEntity.estado,
                        estudianteId = studentEntity.estudianteId,
                        semestre = studentEntity.semestre,
                        tipo = studentEntity.tipo,
                        colegioId = studentEntity.colegioId
                )

            } else if (userEntity.rol.uppercase() == "DOCENTE") {
                // El usuario es un profesor
                // Obtenemos la informacion del profesor por su userId
                val teacherEntity = teacherRepository.findByUserIdAndEstado(userId, true)
                userDto = TeacherAuxDto(
                        userId = userEntity.userId,
                        username = userEntity.username,
                        secret = userEntity.secret,
                        rol = userEntity.rol,
                        personaId = userEntity.personaId,
                        nombre = personaEntity.nombre,
                        apellidoPaterno = personaEntity.apellidoPaterno,
                        apellidoMaterno = personaEntity.apellidoMaterno,
                        carnetIdentidad = personaEntity.carnetIdentidad,
                        fechaNacimiento = personaEntity.fechaNacimiento,
                        correo = personaEntity.correo,
                        genero = personaEntity.genero,
                        celular = personaEntity.celular,
                        descripcion = personaEntity.descripcion,
                        uuidFoto = personaEntity.uuidFoto,
                        uuidPortada = personaEntity.uuidPortada,
                        direccion = personaEntity.direccion,
                        fechaRegistro = personaEntity.fechaRegistro,
                        estadoCivil = personaEntity.estadoCivil,
                        estado = personaEntity.estado,
                        docenteId = teacherEntity.docenteId,
                        tipo = teacherEntity.tipo
                )

            } else {
                LOGGER.error("El rol del usuario no es valido")
                throw UcbException("El rol del usuario no es valido")
            }
        }
        LOGGER.info("Se encontraron los datos del usuario con id: $userId")
        return userDto
    }
}