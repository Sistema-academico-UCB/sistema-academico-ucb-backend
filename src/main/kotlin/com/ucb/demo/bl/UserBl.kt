package com.ucb.demo.bl

import com.ucb.demo.dao.Friend
import com.ucb.demo.dao.Notification
import com.ucb.demo.dao.repository.*
import com.ucb.demo.dto.StudentAuxDto
import com.ucb.demo.dto.TeacherAuxDto
import com.ucb.demo.dto.UserDto
import com.ucb.demo.util.UcbException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserBl @Autowired constructor(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
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


    /**
     * Método para obtener todos los amigos de un usuario
     */
    fun getFriends(userId: Long): List<UserDto> {
        LOGGER.info("Iniciando logica para obtener los amigos del usuario con id: $userId")
        val friendsList = friendRepository.findAllByUsuarioIdUsuarioOrAmigoIdUsuarioAndAceptadoIsTrue(userId, userId)
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
                    // Crear la solicitud de amistad
                    val notification = Notification(
                        emisorId = userId,
                        receptorId = friendId,
                        mensaje = "${user.username} te ha enviado una solicitud de amistad :3",
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
     * Método para obtener las solicitudes de amistad
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
     */
    fun getFriendStatus (userId: Long, friendId: Long): Number {
        LOGGER.info("Iniciando logica para saber si el usuario con id: $userId y el usuario con id: $friendId son amigos")
        val friend = friendRepository.findByUsuarioIdUsuarioAndAmigoIdUsuarioAndAceptadoIsTrue(userId, friendId)
        if (friend == null) {
            val notification = notificationRepository.findByEmisorIdAndReceptorId(userId, friendId)
            if (notification == null) {
                LOGGER.info("El usuario con id: $userId y el usuario con id: $friendId no se mandaron solicitudes de amistad")
                return 2
            } else {
                if (notification.estatus) {
                    LOGGER.info("El usuario con id: $userId y el usuario con id: $friendId se rechazaron la solicitud de amistad")
                    return 2
                } else {
                    LOGGER.info("El usuario con id: $userId y el usuario con id: $friendId no respondieron la solicitud de amistad")
                    return 3
                }
            }
        } else {
            LOGGER.info("El usuario con id: $userId y el usuario con id: $friendId son amigos")
            return 1
        }
    }

}