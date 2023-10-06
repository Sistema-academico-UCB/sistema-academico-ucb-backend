package com.ucb.demo.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.ucb.demo.bl.SecurityBl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthUtil @Autowired constructor(){

    //Logger
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(SecurityBl::class.java.name)
    }

    /**
     * Recibimos el token JWT y lo verificamos para determinar si el usuario esta autenticado,
     * si es correcto retornamos el subject del token,
     * de lo contrario lanzamos una excepción UcbException
     * @param jwtToken
     * @return subject
     */
    fun isUserAuthenticated(jwtToken: String): String? {
        LOGGER.info("Comenzando proceso de verificación de token JWT")
        val subject: String?
        try {
            subject = JWT.require(Algorithm.HMAC256(SecurityBl.JWT_SECRET))
                    .build()
                    .verify(jwtToken)
                    .subject
            LOGGER.info("El usuario está autenticado")
        } catch (exception: JWTVerificationException) {
            LOGGER.error("El usuario no está autenticado")
            throw UcbException("El usuario no está autenticado")
        }
        return subject
    }

    /**
     * Método para obtener el token de un usuario autenticado
     * que se encuentra en la cabecera de la petición
     * @param headers
     * @return token
     */
    fun getTokenFromHeader(headers: Map<String, String>): String {
        if (headers["Authorization"] == null && headers["authorization"] == null) {
            throw UcbException("No se ha enviado el token de autorización")
        }

        var jwt: String = ""
        jwt = if (headers["Authorization"] != null) {
            headers["Authorization"]!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        } else {
            headers["authorization"]!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        }
        return jwt
    }

}