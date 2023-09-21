package com.ucb.demo.bl
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ucb.demo.dao.userRepository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserBl @Autowired constructor(
    private val userRepository: UserRepository
){
    // Logger
    companion object {
        val objectMapper = jacksonObjectMapper()
        val LOGGER: Logger = LoggerFactory.getLogger(UserBl::class.java.name)
    }
}