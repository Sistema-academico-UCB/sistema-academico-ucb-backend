package com.ucb.demo.bl

import com.ucb.demo.dao.repository.CommentRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.ucb.demo.dto.CommentDto
import com.ucb.demo.dao.Comment
import com.ucb.demo.util.UcbException

@Service
class CommentBl @Autowired constructor(
    private val commentRepository: CommentRepository,
){

    //Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(CommentBl::class.java)
    }

    /**
     * Funcion para crear un comentario
     * @param commentDto
     * @return Long
     */
    fun createComment(commentDto: CommentDto): Long {
        LOGGER.info("BL - Iniciando logica para crear un comentario")
        try {
            val comment = Comment()
            comment.userId = commentDto.userId
            comment.publicacionId = commentDto.publicacionId
            comment.descripcion = commentDto.descripcion
            comment.fecha = commentDto.fecha
            comment.estado = commentDto.estado
            val commentCreated = commentRepository.save(comment)
            LOGGER.info("BL - Comentario creado")
            return commentCreated.respuestaId
        } catch (e: Exception) {
            LOGGER.error("BL - Error al crear un comentario: ${e.message}")
            throw UcbException("Error al crear el comentario")
        }
    }

    /**
     * Funcion para eliminar un comentario por su id de manera logica
     * @param commentId
     */
    fun deleteComment(commentId: Long): String {
        LOGGER.info("BL - Iniciando logica para eliminar un comentario")
        try {
            val comment = commentRepository.findById(commentId)
            if (comment.isPresent) {
                comment.get().estado = false
                commentRepository.save(comment.get())
                LOGGER.info("BL - Comentario eliminado")
                return "Comentario eliminado"
            }
            return "Comentario no encontrado"
        } catch (e: Exception) {
            LOGGER.error("BL - Error al eliminar un comentario: ${e.message}")
            throw UcbException("Error al eliminar el comentario")
        }
    }
}