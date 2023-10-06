package com.ucb.demo.api

import com.ucb.demo.bl.UserBl
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
}