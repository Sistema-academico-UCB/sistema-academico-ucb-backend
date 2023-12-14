package com.ucb.demo.bl

import at.favre.lib.crypto.bcrypt.BCrypt
import com.ucb.demo.dao.Friend
import com.ucb.demo.dao.Notification
import com.ucb.demo.dao.repository.*
import com.ucb.demo.dto.*
import com.ucb.demo.util.UcbException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserBl @Autowired constructor(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val studentCareerRepository: StudentCareerRepository,
    private val teacherRepository: TeacherRepository,
    private val personaRepository: PersonaRepository,
    private val friendRepository: FriendRepository,
    private val notificationRepository: NotificationRepository
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
            if(personaEntity == null){
                LOGGER.error("No se encontraron los datos de la persona con id: ${userEntity.personaId}")
                throw UcbException("No se encontraron los datos de la persona con id: ${userEntity.personaId}")
            }
            // Comprobar si es un estudiante o un profesor
            if (userEntity.rol.uppercase() == "ESTUDIANTE") {
                // El usuario es un estudiante
                // Obtenemos la informacion del estudiante por su userId
                val studentEntity = studentRepository.findByUserIdAndEstado(userId, true)
                if (studentEntity == null) {
                    LOGGER.error("No se encontraron los datos del estudiante con id: ${userEntity.personaId}")
                    throw UcbException("No se encontraron los datos del estudiante con id: ${userEntity.personaId}")
                }
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
                if(teacherEntity == null){
                    LOGGER.error("No se encontraron los datos del profesor con id: ${userEntity.personaId}")
                    throw UcbException("No se encontraron los datos del profesor con id: ${userEntity.personaId}")
                }
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

        }else{
            LOGGER.error("No se encontraron los datos del usuario con id: $userId")
            throw UcbException("No se encontraron los datos del usuario con id: $userId")
        }
        LOGGER.info("Se encontraron los datos del usuario con id: $userId")
        return userDto
    }

    /**
     * Método para eliminar lógicamente un usuario por su id
     * @param userId
     */
    fun deleteUserById(userId: Long): String {
        LOGGER.info("Iniciando lógica para eliminar lógicamente un usuario por su id")
        val user = userRepository.findByUserIdAndEstado(userId, true)
        if (user != null) {
            user.estado = false
            userRepository.save(user)
            LOGGER.info("Se eliminó lógicamente el usuario con id: $userId")
            // Eliminar logicamente su registro de estudiante o profesor
            if (user.rol.uppercase() == "ESTUDIANTE") {
                val student = studentRepository.findByUserIdAndEstado(userId, true)
                if (student != null) {
                    student.estado = false
                    studentRepository.save(student)
                    LOGGER.info("Se eliminó lógicamente el estudiante con id: ${student.estudianteId}")
                    //Eliminamos logicamente su registro de estudiante_carrera
                    val studentCareer = studentCareerRepository.findByEstudianteIdAndEstado(student.estudianteId, true)
                    if (studentCareer != null) {
                        studentCareer[0].estado = false
                        studentCareerRepository.save(studentCareer[0])
                        LOGGER.info("Se eliminó lógicamente el registro de estudiante_carrera con id: ${studentCareer[0].estudianteId}")
                    }

                }
            } else if (user.rol.uppercase() == "DOCENTE") {
                val teacher = teacherRepository.findByUserIdAndEstado(userId, true)
                if (teacher != null) {
                    teacher.estado = false
                    teacherRepository.save(teacher)
                    LOGGER.info("Se eliminó lógicamente el profesor con id: ${teacher.docenteId}")
                }
            }
            //Eliminar logicamente su registro en persona
            val person = personaRepository.findByPersonaIdAndEstado(user.personaId, true)
            if (person != null) {
                person.estado = false
                personaRepository.save(person)
                LOGGER.info("Se eliminó lógicamente la persona con id: ${person.personaId}")
            }
            return "Se eliminó lógicamente el usuario con id: $userId"
        } else {
            LOGGER.error("No existe un usuario con id: $userId")
            throw UcbException("No existe un usuario con id: $userId")
        }
    }

    /**
     * Método para actualizar la contraseña del propio usuario
     * @param userId
     * @param password
     */
    fun updatePassword(userId: Long, password: PasswordUpdateDto): String {
        LOGGER.info("Iniciando lógica para actualizar la contraseña del propio usuario")
        //Verificar que la contraseña nueva y la confirmación de la contraseña nueva coincidan
        if (password.newPassword != password.confirmNewPassword) {
            LOGGER.error("La contraseña nueva y la confirmación de la contraseña nueva no coinciden")
            throw UcbException("La contraseña nueva y la confirmación de la contraseña nueva no coinciden")
        }
        val user = userRepository.findByUserIdAndEstado(userId, true)
        if (user != null) {
            //Verificar si la contraseña actual coincide
            val currentPasswordInBCrypt = user.secret
            val resultBCrypt: BCrypt.Result = BCrypt.verifyer().verify(password.currentPassword.toCharArray(), currentPasswordInBCrypt)
            if (!resultBCrypt.verified) {
                LOGGER.error("La contraseña ingresada no coincide con la contraseña actual en la base de datos")
                throw UcbException("La contraseña ingresada no coincide con la contraseña actual en la base de datos")
            }
            user.secret = BCrypt.withDefaults().hashToString(12, password.newPassword.toCharArray())
            userRepository.save(user)
            LOGGER.info("Se actualizó la contraseña del usuario con id: $userId")
            return "Se actualizó la contraseña del usuario con id: $userId"
        } else {
            LOGGER.error("No existe un usuario con id: $userId")
            throw UcbException("No existe un usuario con id: $userId")
        }
    }

    /**
     * Método para actualizar la contraseña del propio usuario sin verificar la contraseña actual
     * @param userId
     * @param password
     * @return String
     */
    fun updatePasswordWithout(userId: Long, password: PasswordDto): String {
        LOGGER.info("Iniciando lógica para actualizar la contraseña del propio usuario sin verificar la contraseña actual")
        val user = userRepository.findByUserIdAndEstado(userId, true)
        if (user != null) {
            user.secret = BCrypt.withDefaults().hashToString(12, password.newPassword.toCharArray())
            userRepository.save(user)
            LOGGER.info("Se actualizó la contraseña del usuario con id: $userId")
            return "Se actualizó la contraseña del usuario con id: $userId"
        } else {
            LOGGER.error("No existe un usuario con id: $userId")
            throw UcbException("No existe un usuario con id: $userId")
        }
    }



    //===================================FRIENDS=======================================

    /**
     * Método para obtener todos los amigos de un usuario por su id
     * @param userId
     * @return List<UserDto>
     */
    fun getFriends(userId: Long): List<UserDto> {
        LOGGER.info("Iniciando logica para obtener los amigos del usuario con id: $userId")
        val friendsList1 = friendRepository.findAllByUsuarioIdUsuarioAndAceptadoIsTrue(userId)
        val friendsList2 = friendRepository.findAllByAmigoIdUsuarioAndAceptadoIsTrue(userId)
        val friendsList = mutableListOf<Friend>()
        if (friendsList1 != null) {

            friendsList.addAll(friendsList1)
        }
        if (friendsList2 != null) {
            friendsList.addAll(friendsList2)
        }
        if (friendsList != null) {
            // Obtener los amigos del usuario
            val friends = mutableListOf<UserDto>()
            for (friend in friendsList) {
                if (friend.usuarioIdUsuario == userId) {
                    val friendUser = getUserById(friend.amigoIdUsuario)
                    if (friendUser != null) {
                        friends.add(friendUser)
                    }
                } else {
                    val friendUser = getUserById(friend.usuarioIdUsuario)
                    if (friendUser != null) {
                        friends.add(friendUser)
                    }
                }
            }
            LOGGER.info("Se encontraron los amigos del usuario con id: $userId")
            return friends
        } else {
            LOGGER.error("El usuario con id: $userId no existe")
            throw UcbException("El usuario con id: $userId no existe")
        }
    }

    /**
     * Método para mandar una solicitud de amistad
     * @param userId
     * @param friendId
     */
    fun sendFriendRequest(userId: Long, friendId: Long): String {
        var message = "Se envio la solicitud de amistad";
        LOGGER.info("Iniciando logica para mandar una solicitud de amistad")
        // Comprobar si el usuario existe
        val user = userRepository.findByUserIdAndEstado(userId, true)
        if (user != null) {
            // Comprobar si el amigo existe
            val friend = userRepository.findByUserIdAndEstado(friendId, true)
            if (friend != null) {
                // Comprobar si ya existe una solicitud de amistad
                val friendRequest = notificationRepository.findByEmisorIdAndReceptorId(userId, friendId)
                if (friendRequest != null) {
                    if (friendRequest.estatus) {
                        friendRequest.estatus = false
                        notificationRepository.save(friendRequest)
                        message = "Se envio la solicitud de amistad de nuevo"
                    } else {
                        LOGGER.error("Ya existe una solicitud de amistad entre el usuario con id: $userId y el usuario con id: $friendId")
                        throw UcbException("Ya existe una solicitud de amistad entre el usuario con id: $userId y el usuario con id: $friendId")
                    }
                } else {
                    val person = this.personaRepository.findByPersonaIdAndEstado(user.personaId, true);
                    if (person == null) {
                        LOGGER.error("No se encontraron los datos de la persona con id: ${user.personaId}")
                        throw UcbException("No se encontraron los datos de la persona con id: ${user.personaId}")
                    }
                    // Crear la solicitud de amistad
                    val notification = Notification(
                        emisorId = userId,
                        receptorId = friendId,
                        mensaje = "${person.nombre} ${person.apellidoPaterno} ${person.apellidoMaterno} te ha enviado una solicitud de amistad :3",
                        fechaEnvio = Date(),
                        estatus = false
                    )
                    notificationRepository.save(notification)
                }
            }
        }
        return message
    }

    /**
     * Método para responder una solicitud de amistad
     * @param userId
     * @param friendId
     * @param response
     */
    fun respondFriendRequest(userId: Long, friendId: Long, response: Boolean): String {
        var message = "Se respondio la solicitud de amistad";
        LOGGER.info("Iniciando logica para responder una solicitud de amistad")
        // Comprobar si el usuario existe
        val user = userRepository.findByUserIdAndEstado(userId, true)
        if (user != null) {
            // Comprobar si el amigo existe
            val friend = userRepository.findByUserIdAndEstado(friendId, true)
            if (friend != null) {
                // Comprobar si ya existe una solicitud de amistad
                val friendRequest = notificationRepository.findByEmisorIdAndReceptorId(friendId, userId)
                if (friendRequest != null) {
                    if (!friendRequest.estatus) {
                        friendRequest.estatus = true
                        notificationRepository.save(friendRequest)
                        if (response) {
                            // Crear la amistad
                            val friend = Friend(
                                usuarioIdUsuario = friendId,
                                amigoIdUsuario = userId,
                                aceptado = true
                            )
                            friendRepository.save(friend)
                            message = "Se acepto la solicitud de amistad"
                        } else {
                            message = "Se rechazo la solicitud de amistad"
                        }
                    } else {
                        LOGGER.error("Ya se respondio la solicitud de amistad entre el usuario con id: $userId y el usuario con id: $friendId")
                        throw UcbException("Ya se respondio la solicitud de amistad entre el usuario con id: $userId y el usuario con id: $friendId")
                    }
                } else {
                    LOGGER.error("No existe una solicitud de amistad entre el usuario con id: $userId y el usuario con id: $friendId")
                    throw UcbException("No existe una solicitud de amistad entre el usuario con id: $userId y el usuario con id: $friendId")
                }
            }
        }
        return message
    }

    /**
     * Método para obtener las solicitudes de amistad de un usuario por su id
     * @param userId
     * @return Lista de solicitudes de amistad
     */
    fun getFriendRequests(userId: Long): List<Notification> {
        LOGGER.info("Iniciando logica para obtener las solicitudes de amistad del usuario con id: $userId")
        val friendRequests = notificationRepository.findAllByReceptorIdAndEstatus(userId, false)
        if (friendRequests != null) {
            LOGGER.info("Se encontraron las solicitudes de amistad del usuario con id: $userId")
            return friendRequests
        } else {
            LOGGER.error("El usuario con id: $userId no tiene solicitudes de amistad pendientes")
            throw UcbException("El usuario con id: $userId no tiene solicitudes de amistad pendientes")
        }
    }

    /**
     * Método para saber si dos usuarios son amigos
     * @param userId
     * @param friendId
     * @return 1 si son amigos, 0 si no son amigos, -1 si no existe alguno de los dos usuarios
     */
    fun getFriendStatus(userId: Long, friendId: Long): Int {
        LOGGER.info("Iniciando lógica para saber si el usuario con id: $userId y el usuario con id: $friendId son amigos")

        val friend1 = friendRepository.findByUsuarioIdUsuarioAndAmigoIdUsuarioAndAceptadoIsTrue(userId, friendId)
        val friend2 = friendRepository.findByUsuarioIdUsuarioAndAmigoIdUsuarioAndAceptadoIsTrue(friendId, userId)

        if (friend1 != null || friend2 != null) {
            LOGGER.info("El usuario con id: $userId y el usuario con id: $friendId son amigos")
            return 1
        }

        val notification1 = notificationRepository.findByEmisorIdAndReceptorId(userId, friendId)
        val notification2 = notificationRepository.findByEmisorIdAndReceptorId(friendId, userId)

        if (notification1 == null && notification2 == null) {
            LOGGER.info("El usuario con id: $userId y el usuario con id: $friendId no se mandaron solicitudes de amistad")
            return 2
        } else {
            if ((notification1 != null && notification1.estatus) || (notification2 != null && notification2.estatus)) {
                LOGGER.info("El usuario con id: $userId y el usuario con id: $friendId han rechazado la solicitud de amistad")
                return 2
            } else {
                LOGGER.info("El usuario con id: $userId y el usuario con id: $friendId está a la espera de respuesta")
                return 3
            }
        }
    }

    /**
     * Método para eliminar lógicamente una registro de amistad
     * @param userId
     * @param friendId
     */

    fun deleteFriend(userId: Long, friendId: Long): String {
        LOGGER.info("Iniciando lógica para eliminar lógicamente una registro de amistad")
        val friend1 = friendRepository.findByUsuarioIdUsuarioAndAmigoIdUsuarioAndAceptadoIsTrue(userId, friendId)
        val friend2 = friendRepository.findByUsuarioIdUsuarioAndAmigoIdUsuarioAndAceptadoIsTrue(friendId, userId)

        if (friend1 != null || friend2 != null) {
            if (friend1 != null) {
                friend1.aceptado = false
                friendRepository.save(friend1)
            } else {
                friend2!!.aceptado = false
                friendRepository.save(friend2)
            }
            LOGGER.info("Se eliminó lógicamente la amistad entre el usuario con id: $userId y el usuario con id: $friendId")
            return "Se eliminó lógicamente la amistad entre el usuario con id: $userId y el usuario con id: $friendId"
        } else {
            LOGGER.error("No existe una amistad entre el usuario con id: $userId y el usuario con id: $friendId")
            throw UcbException("No existe una amistad entre el usuario con id: $userId y el usuario con id: $friendId")
        }
    }

    /** Método para actualizar el perfil del propio usuario
     *  @param userId
     *  @return UserDto
     */
    fun updateUserProfile(userId: Long, userProfileDto: UserProfileDto): UserDto? {
        var userDto: UserDto? = null
        val userEntity = userRepository.findByUserIdAndEstado(userId, true)
        if (userEntity != null) {
            val personaEntity = personaRepository.findByPersonaIdAndEstado(userEntity.personaId, true)
            if (personaEntity == null) {
                LOGGER.error("No se encontraron los datos de la persona con id: ${userEntity.personaId}")
                throw UcbException("No se encontraron los datos de la persona con id: ${userEntity.personaId}")
            }
            personaEntity.descripcion = userProfileDto.descripcion;
            personaEntity.uuidPortada = userProfileDto.uuidPortada;
            personaEntity.uuidFoto = userProfileDto.uuidFoto;
            personaRepository.save(personaEntity);
            userDto = getUserById(userId)
        }else{
            LOGGER.error("No se encontraron los datos del usuario con id: $userId")
            throw UcbException("No se encontraron los datos del usuario con id: $userId")
        }
        return userDto
    }

    /**
     * Método para verificar la existencia de un usuario por su correo
     * @param correo
     * @return Boolean
     */
    fun verifyEmail(correo: String): Boolean {
        LOGGER.info("Iniciando lógica para verificar la existencia de un usuario por su correo")
        val user = personaRepository.existsByCorreoAndEstado(correo, true)
        if (user) {
            LOGGER.info("Existe un usuario con el correo: $correo")
            return true
        } else {
            LOGGER.info("No existe un usuario con el correo: $correo")
            return false
        }
    }

}