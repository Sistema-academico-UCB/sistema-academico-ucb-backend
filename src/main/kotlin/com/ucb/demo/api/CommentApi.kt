package com.ucb.demo.api

import com.ucb.demo.bl.CommentBl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.ucb.demo.dto.CommentDto
import com.ucb.demo.dto.ResponseDto

@RestController
@RequestMapping("/api/v1/")
class CommentApi @Autowired constructor(
    private val commentBl: CommentBl,
){

    //Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(CommentApi::class.java)
    }

    /**
     * Endpoint POST para crear un comentario
     * @param commentDto
     * @return ResponseDto<Long>
     */
    @PostMapping("/comment")
    fun createComment(@RequestBody commentDto: CommentDto): ResponseDto<Long> {
        LOGGER.info("Iniciando logica para crear un comentario")
        return try {
            val commentId = commentBl.createComment(commentDto)
            ResponseDto(
                success = true,
                message = "Comentario creado",
                data = commentId
            )
        } catch (e: Exception) {
            ResponseDto(
                success = false,
                message = "Error al crear el comentario",
                data = null
            )
        }
    }

    /**
     * Endpoint DELETE para eliminar un comentario de manera logica por su id
     * @param commentId
     * @return ResponseDto<String>
     */
    @DeleteMapping("/comment/{commentId}")
    fun deleteComment(@PathVariable commentId: Long): ResponseDto<String> {
        LOGGER.info("Iniciando logica para eliminar un comentario")
        return try {
            val commentDeleted = commentBl.deleteComment(commentId)
            ResponseDto(
                success = true,
                message = commentDeleted,
                data = null
            )
        } catch (e: Exception) {
            ResponseDto(
                success = false,
                message = "Error al eliminar el comentario",
                data = null
            )
        }
    }
}