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
     * @param userId
     * @return ResponseDto<UserDto>
     */
    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): ResponseDto<UserDto> {
        LOGGER.info("Iniciando logica para obtener un usuario por su id")
        return try {
            val userDto = userBl.getUserById(userId)
            ResponseDto(
                success = true,
                message = "Usuario obtenido",
                data = userDto
            )
        }catch (ex: UcbException){
            ResponseDto(
                success = false,
                message = ex.message!!,
                data = null
            )
        }
    }


    /**
     * Endpoint GET para obtener los amigos del usuario por su id
     * @param userId
     * @return ResponseDto<List<UserDto>>
     */
    @GetMapping("/{userId}/friend")
    fun getFriends(@PathVariable userId: Long): ResponseDto<List<UserDto>> {
        LOGGER.info("Iniciando logica para obtener los amigos del usuario")
        return try {
            val friends = userBl.getFriends(userId)
            ResponseDto(
                success = true,
                message = "Amigos obtenidos",
                data = friends
            )
        }catch (ex: UcbException){
            ResponseDto(
                success = false,
                message = ex.message!!,
                data = null
            )
        }

    }


    /**
     * Endpoint POST para mandar una solicitud de amistad
     * @param headers
     * @param friendId
     * @return ResponseDto<String>
     */
    @PostMapping("/friend/{friendId}")
    fun sendFriendRequest(@RequestHeader headers: Map<String, String>, @PathVariable friendId: Long): ResponseDto<String> {
        LOGGER.info("Iniciando logica para mandar una solicitud de amistad")
        val userId: String? = authUtil.isUserAuthenticated(authUtil.getTokenFromHeader(headers))
        return try {
            val response: String = userBl.sendFriendRequest(userId!!.toLong(), friendId)
            ResponseDto(
                success = true,
                message = "Solicitud de amistad enviada",
                data = response
            )
        }catch (ex: UcbException){
            ResponseDto(
                success = false,
                message = ex.message!!,
                data = null
            )
        }

    }

    /**
     * Endpoint PUT para responder una solicitud de amistad
     * @param headers
     * @param friendId
     * @param response
     * @return ResponseDto<String>
     */
    @PutMapping("/friend/{friendId}/{response}")
    fun respondFriendRequest(@RequestHeader headers: Map<String, String>, @PathVariable friendId: Long, @PathVariable response: Boolean): ResponseDto<String> {
        LOGGER.info("Iniciando logica para responder una solicitud de amistad")
        val userId: String? = authUtil.isUserAuthenticated(authUtil.getTokenFromHeader(headers))
        return try {
            val response: String = userBl.respondFriendRequest(userId!!.toLong(), friendId, response)
            ResponseDto(
                success = true,
                message = "Solicitud de amistad respondida",
                data = response
            )
        }catch (ex: UcbException) {
            ResponseDto(
                success = false,
                message = ex.message!!,
                data = null
            )
        }
    }

    /**
     * Endpoint GET para obtener todas las solicitudes de amistad
     * @param headers
     * @return ResponseDto<List<Notification>>
     */
    @GetMapping("/friend/request")
    fun getFriendRequests(@RequestHeader headers: Map<String, String>): ResponseDto<List<Notification>> {
        LOGGER.info("Iniciando logica para obtener todas las solicitudes de amistad")
        val userId: String? = authUtil.isUserAuthenticated(authUtil.getTokenFromHeader(headers))
        return try {
            val requests = userBl.getFriendRequests(userId!!.toLong())
            ResponseDto(
                success = true,
                message = "Solicitudes de amistad obtenidas",
                data = requests
            )
        }catch (ex: UcbException) {
            ResponseDto(
                success = false,
                message = ex.message!!,
                data = null
            )
        }

    }

    /**
     * Endpoint GET para saber si son amigos, solicitud pendiente o si no son amigos
     * @param headers
     * @param friendId
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
     * @param headers
     * @param friendId
     * @return ResponseDto<String>
     */
    @DeleteMapping("/friend/{friendId}")
    fun deleteFriend(@RequestHeader headers: Map<String, String>, @PathVariable friendId: Long): ResponseDto<String> {
        LOGGER.info("Iniciando logica para eliminar un amigo de la lista de amigos")
        val userId: String? = authUtil.isUserAuthenticated(authUtil.getTokenFromHeader(headers))
        return try {
            val response: String = userBl.deleteFriend(userId!!.toLong(), friendId)
            ResponseDto(
                success = true,
                message = "Amigo eliminado",
                data = response
            )
        }catch (ucbException: UcbException){
            ResponseDto(
                success = false,
                message = ucbException.message!!,
                data = null
            )
        }

    }


}