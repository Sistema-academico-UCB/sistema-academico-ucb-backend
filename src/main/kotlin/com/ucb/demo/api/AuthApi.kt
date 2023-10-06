package com.ucb.demo.api

import com.ucb.demo.bl.SecurityBl
import com.ucb.demo.dto.AuthReqDto
import com.ucb.demo.dto.AuthResDto
import com.ucb.demo.dto.ResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class AuthApi @Autowired constructor(
        private val securityBl: SecurityBl
) {

    // Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(AuthApi::class.java)
    }

    /**
     * Endpoint POST para realizar la autenticacion de un usuario
     * @param authReqDto
     * @return ResponseDto<AuthResDto>
     */
    @PostMapping("/auth")
    fun authentication(@RequestBody authReqDto: AuthReqDto): ResponseDto<AuthResDto> {
        var responseDto: ResponseDto<AuthResDto>;
        if(authReqDto != null && authReqDto.email != null && authReqDto.password != null) {
            AuthApi.LOGGER.info("Inicio de autenticacion para el usuario con correo ${authReqDto.email}")
            try {
                val authResDto = securityBl.authenticateUser(authReqDto)
                responseDto = ResponseDto(
                        success = true,
                        message = "Autenticacion exitosa",
                        data = authResDto
                )
                AuthApi.LOGGER.info("Autenticacion exitosa")
            }catch (e: Exception) {
                AuthApi.LOGGER.error("Error: ", e.message)
                responseDto = ResponseDto(
                        success = false,
                        message = "Error al autenticar",
                        data = null
                )
            }
        } else {
            responseDto = ResponseDto(
                    success = false,
                    message = "Los datos de autenticacion son incorrectos",
                    data = null
            )
            AuthApi.LOGGER.info("Los datos de autenticacion son incorrectos")
        }
        return responseDto
    }
}