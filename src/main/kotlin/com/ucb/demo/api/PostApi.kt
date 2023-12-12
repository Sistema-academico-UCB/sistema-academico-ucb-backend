package com.ucb.demo.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.ucb.demo.bl.PostBl
import com.ucb.demo.dto.PostDto
import com.ucb.demo.dto.ResponseDto
import com.ucb.demo.dto.PostResponseDto

import com.ucb.demo.util.UcbException


@RestController
@RequestMapping("/api/v1/")
class PostApi @Autowired constructor(
    private val postBl: PostBl,
){

    //Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PostApi::class.java)
    }

    //==============================================================
    // POST
    /**
     * Endpoint POST para crear una publicacion
     * @param postDto
     * @return ResponseDto<Long>
     */
    @PostMapping("/post")
    fun createPost(@RequestBody postDto: PostDto): ResponseDto<Long> {
        LOGGER.info("Iniciando logica para crear una publicacion")
        return try {
            val postId = postBl.createPost(postDto)
            ResponseDto(
                success = true,
                message = "Publicacion creada",
                data = postId
            )
        } catch (e: UcbException) {
            ResponseDto(
                success = false,
                message = "Error al crear la publicacion",
                data = null
            )
        } 
    }

    /**
     * Endpoint DELETE para eliminar una publicacion por su id
     * @param postId
     * @return ResponseDto<String>
     */
    @DeleteMapping("/post/{postId}")
    fun deletePost(@PathVariable postId: Long): ResponseDto<String> {
        LOGGER.info("Iniciando logica para eliminar una publicacion")
        return try {
            postBl.deletePost(postId)
            ResponseDto(
                success = true,
                message = "Publicacion eliminada",
                data = "Publicacion eliminada"
            )
        } catch (e: UcbException) {
            ResponseDto(
                success = false,
                message = "Error al eliminar la publicacion",
                data = null
            )
        }
    }

    /**
     * Endpoint GET para obtener las publicaciones de un usuario
     * @param userId
     * @return ResponseDto<List<PostResponseDto>>
     */
    @GetMapping("user/{userId}/posts")
    fun getPostsByUserId(@PathVariable userId: Long): ResponseDto<List<PostResponseDto>> {
        LOGGER.info("Iniciando logica para obtener las publicaciones de un usuario")
        return try {
            val posts = postBl.getPostsByUserId(userId)
            ResponseDto(
                success = true,
                message = "Publicaciones obtenidas",
                data = posts
            )
        } catch (e: UcbException) {
            ResponseDto(
                success = false,
                message = "Error al obtener las publicaciones",
                data = null
            )
        }
    }

}