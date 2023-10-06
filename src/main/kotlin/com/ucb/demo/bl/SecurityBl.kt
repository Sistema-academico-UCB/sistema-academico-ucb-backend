package com.ucb.demo.bl

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.ucb.demo.dao.repository.PersonaRepository
import com.ucb.demo.dao.repository.UserRepository
import com.ucb.demo.dto.AuthReqDto
import com.ucb.demo.dto.AuthResDto
import com.ucb.demo.util.UcbException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class SecurityBl @Autowired constructor(
        private val userRepository: UserRepository,
        private val personaRepository: PersonaRepository

){

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(StudentBl::class.java.name)
        const val JWT_SECRET: String = "ucb2023"
    }

    /**
     * Este método se encarga de realizar la autenticación de un usuario del sistema
     * la constraseña recibida se compara con su equivalente en BCRYPT presente en la
     * base de datos
     * @param credentials
     * @return AuthResDto
     */
    fun authenticateUser(credentials: AuthReqDto): AuthResDto?{
        var result: AuthResDto? = null
        LOGGER.info("Comenzando proceso de autenticación con: $credentials")
        // Obtener el usuario por su correo
        val persona = personaRepository.findByCorreoAndEstado(credentials.email, true)
        if(persona!=null){
            val user = userRepository.findByPersonaIdAndEstado(persona.personaId, true)
            val currentPasswordInBCrypt = user.secret
            //println("Se obtuvo la siguiente contraseña de bbdd: $currentPasswordInBCrypt")
            val resultBCrypt: BCrypt.Result = BCrypt.verifyer().verify(credentials.password.toCharArray(), currentPasswordInBCrypt)
            // Verificar si la contraseña coincide
            if (resultBCrypt.verified) {
                //println("La contraseña coincide")
                // Crear el token de autenticación con el usuario y sus roles y un tiempo de expiracion largo
                result = generateTokenJWT(user.userId.toString(), 30000)
                //println(result)
                LOGGER.info("Se generó el token JWT: $result")
            } else {
                //println("Las contraseñas no coinciden")
                throw UcbException("Contraseña incorrecta")
            }

        }else{
            LOGGER.error("Usuario no encontrado")
        }
        return result
    }


    /**
     * Este método se encarga de generar el token JWT
     * @param subject
     * @param expirationTimeInSeconds
     * @return AuthenticationResponse
     */
    fun generateTokenJWT(subject: String, expirationTimeInSeconds: Int): AuthResDto? {
        var result : AuthResDto? = null
        // Generar el token
        try {
            // Establecemos el algoritmo a utilizar
            val algorithm: Algorithm = Algorithm.HMAC256(JWT_SECRET)
            val token: String = JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(Date(System.currentTimeMillis() + expirationTimeInSeconds * 1000))
                    .withIssuer("UCB")
                    .withClaim("refresh", false)
                    .sign(algorithm)

            // Refresh token
            val refreshToken: String = JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(Date(System.currentTimeMillis() + expirationTimeInSeconds * 1000 * 2))
                    .withIssuer("UCB")
                    .withClaim("refresh", true)
                    .sign(algorithm)

            // Asignamos el token y el refresh token al objeto de respuesta
            result = AuthResDto(token, refreshToken)
        } catch (exception: JWTCreationException) {
            // Invalid Signing configuration
            println("Ocurrió un error al generar el token")
            LOGGER.error("Ocurrió un error al generar el token", exception)
            //throw FrankieException("Ocurrió un error al autenticar al usuario", exception)
        }
        return result
    }





}