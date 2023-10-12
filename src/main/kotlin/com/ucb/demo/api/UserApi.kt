package com.ucb.demo.api

import com.ucb.demo.bl.UserBl
import com.ucb.demo.dao.Notification
import com.ucb.demo.dto.ResponseDto
import com.ucb.demo.dto.UserDto
import com.ucb.demo.util.AuthUtil
import com.ucb.demo.util.UcbException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/user")
class UserApi @Autowired constructor(
        private val userBl: UserBl,
        private val authUtil: AuthUtil
){
    // Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(UserApi::class.java)
    }

    //==============================================================
    /**
     * Endpoint GET para obtener un usuario por su token
     * @param headers
     * @return ResponseDto<UserDto>
     */
    @GetMapping()
    fun getUserFromToken(@RequestHeader headers: Map<String, String>): ResponseDto<UserDto> {
        var responseDto: ResponseDto<UserDto>
        try {
            val userId: String? = authUtil.isUserAuthenticated(authUtil.getTokenFromHeader(headers))
            val userDto = userBl.getUserById(userId!!.toLong())
            responseDto = ResponseDto(userDto, "Se obtuvo el usuario", true)
        } catch (ex: UcbException) {
            responseDto = ResponseDto(null, ex.message+"", false)
        }
        return responseDto
    }


    //==============================================================
    // USUARIO
    /**
     * Endpoint GET para obtener un usuario por su id
     * @return ResponseDto<UserDto>
     */
    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): ResponseDto<UserDto> {
        LOGGER.info("Iniciando logica para obtener un usuario por su id")
        val userDto = userBl.getUserById(userId)
                ?: return ResponseDto(
                        success = false,
                        message = "No existe un usuario con el id: $userId",
                        data = null
                )
        return ResponseDto(
                success = true,
                message = "Usuario obtenido",
                data = userDto
        )
    }

    /**
     * Endpoint GET para obtener los amigos del usuario por su id
     * @return ResponseDto<List<UserDto>>
     */
    @GetMapping("/{userId}/friend")
    fun getFriends(@PathVariable userId: Long): ResponseDto<List<UserDto>> {
        LOGGER.info("Iniciando logica para obtener los amigos del usuario")
        val friends = userBl.getFriends(userId)
        return ResponseDto(
            success = true,
            message = "Amigos obtenidos",
            data = friends
        )
    }

    /**
     * Endpoint POST para mandar una solicitud de amistad
     * @return ResponseDto<String>
     */
    @PostMapping("/friend/{friendId}")
    fun sendFriendRequest(@RequestHeader headers: Map<String, String>, @PathVariable friendId: Long): ResponseDto<String> {
        LOGGER.info("Iniciando logica para mandar una solicitud de amistad")
        val userId: String? = authUtil.isUserAuthenticated(authUtil.getTokenFromHeader(headers))
        val response: String = userBl.sendFriendRequest(userId!!.toLong(), friendId)
        return ResponseDto(
            success = true,
            message = "Solicitud de amistad enviada",
            data = response
        )
    }

    /**
     * Endpoint PUT para responder una solicitud de amistad
     * @return ResponseDto<String>
     */
    @PutMapping("/friend/{friendId}/{response}")
    fun respondFriendRequest(@RequestHeader headers: Map<String, String>, @PathVariable friendId: Long, @PathVariable response: Boolean): ResponseDto<String> {
        LOGGER.info("Iniciando logica para responder una solicitud de amistad")
        val userId: String? = authUtil.isUserAuthenticated(authUtil.getTokenFromHeader(headers))
        val response: String = userBl.respondFriendRequest(userId!!.toLong(), friendId, response)
        return ResponseDto(
            success = true,
            message = "Solicitud de amistad respondida",
            data = response
        )
    }

    /**
     * Endpoint GET para obtener todas las solicitudes de amistad
     * @return ResponseDto<List<Notification>>
     */
    @GetMapping("/friend/request")
    fun getFriendRequests(@RequestHeader headers: Map<String, String>): ResponseDto<List<Notification>> {
        LOGGER.info("Iniciando logica para obtener todas las solicitudes de amistad")
        val userId: String? = authUtil.isUserAuthenticated(authUtil.getTokenFromHeader(headers))
        val requests = userBl.getFriendRequests(userId!!.toLong())
        return ResponseDto(
            success = true,
            message = "Solicitudes de amistad obtenidas",
            data = requests
        )
    }

    /**
     * Endpoint GET para saber si son amigos, solicitud pendiente o si no son amigos
     * @return ResponseDto<Number>
     */
    @GetMapping("/friend/{friendId}")
    fun getFriendStatus(@RequestHeader headers: Map<String, String>, @PathVariable friendId: Long): ResponseDto<Number> {
        LOGGER.info("Iniciando logica para saber si son amigos, solicitud pendiente o si no son amigos")
        val userId: String? = authUtil.isUserAuthenticated(authUtil.getTokenFromHeader(headers))
        val status = userBl.getFriendStatus(userId!!.toLong(), friendId)
        return ResponseDto(
            success = true,
            message = "Estado de amistad obtenido",
            data = status
        )
    }

    /**
     * Endpoint DELETE para eliminar un amigo de la lista de amigos
     * @return ResponseDto<String>
     */
    @DeleteMapping("/friend/{friendId}")
    fun deleteFriend(@RequestHeader headers: Map<String, String>, @PathVariable friendId: Long): ResponseDto<String> {
        LOGGER.info("Iniciando logica para eliminar un amigo de la lista de amigos")
        val userId: String? = authUtil.isUserAuthenticated(authUtil.getTokenFromHeader(headers))
        try {
            val response: String = userBl.deleteFriend(userId!!.toLong(), friendId)
            return ResponseDto(
                success = true,
                message = "Amigo eliminado",
                data = response
            )
        }catch (ucbException: UcbException){
            return ResponseDto(
                success = false,
                message = ucbException.message!!,
                data = null
            )
        }

    }


}