package com.ucb.demo.api
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
import com.ucb.demo.bl.UserBl
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@RestController
@RequestMapping("/api/v1/user")
class UserApi @Autowired constructor(
        private val userBl: UserBl
){
    // Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(UserApi::class.java)
    }

    //==============================================================
    // USUARIO
    /**
     * Endpoint POST para crear un usuario - estudiante
     * @param userDto
     * @return ResponseDto<UserDto>
     */


}