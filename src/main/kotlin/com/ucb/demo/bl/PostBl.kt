package com.ucb.demo.bl

import com.ucb.demo.dao.repository.PostRepository
import com.ucb.demo.dao.repository.CommentRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.ucb.demo.dao.Post
import com.ucb.demo.dto.PostResponseDto
import com.ucb.demo.dto.PostDto
import com.ucb.demo.dto.CommentDto
import com.ucb.demo.util.UcbException

@Service
class PostBl @Autowired constructor(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,

) {
    // Logger
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(PostBl::class.java.name)
    }

    //==============================================================
    // POST
    /**
     * Funcion para crear una publicacion
     * @param postDto
     * @return Long
     */
    fun createPost(postDto: PostDto): Long {
        LOGGER.info("BL - Iniciando logica para crear una publicacion")
        try {
            val post = Post()
            post.userId = postDto.userId
            post.descripcion = postDto.descripcion
            post.fecha = postDto.fecha
            post.estado = postDto.estado
            val postCreated = postRepository.save(post)
            LOGGER.info("BL - Publicacion creada")
            return postCreated.publicacionId
        } catch (e: Exception) {
            LOGGER.error("BL - Error al crear una publicacion: ${e.message}")
            throw UcbException("Error al crear la publicacion")
        }
        
    }

    /**
     * Funcion para eliminar una publicacion por su id de manera logica
     * @param postId
     */
    fun deletePost(postId: Long): String {
        LOGGER.info("BL - Iniciando logica para eliminar una publicacion")
        try {
            val post = postRepository.findById(postId)
            if (post.isPresent) {
                post.get().estado = false
                postRepository.save(post.get())
                LOGGER.info("BL - Publicacion eliminada")
                return "Publicacion eliminada"
            } else {
                LOGGER.error("BL - No existe una publicacion con el id: $postId")
                throw UcbException("No existe una publicacion con el id: $postId")
            }
        } catch (e: Exception) {
            LOGGER.error("BL - Error al eliminar una publicacion: ${e.message}")
            throw UcbException("Error al eliminar la publicacion")
        }
    }

    /**
     * Funcion para obtener las publicaciones de un usuario por su id
     * @param userId
     * @return List<PostResponseDto>
     */
    fun getPostsByUserId(userId: Long): List<PostResponseDto> {
        LOGGER.info("BL - Iniciando logica para obtener las publicaciones de un usuario")
        try {
            var listaPostResponseDto: MutableList<PostResponseDto> = mutableListOf()
            // Obtener las publicaciones de un usuario
            var publicaciones: List<Post> = postRepository.findByUserIdAndEstadoIsTrue(userId)
            // Obtener las respuestas de cada publicacion
            for (publicacion in publicaciones) {
                var respuestas = commentRepository.findByPublicacionIdAndEstadoIsTrue(publicacion.publicacionId)
                // Pasar las respuestas a una lista de CommentDto
                var respuestaList: MutableList<CommentDto> = mutableListOf()
                for (respuesta in respuestas) {
                    var CommentDto = CommentDto(
                        respuestaId = respuesta.respuestaId,
                        publicacionId = respuesta.publicacionId,
                        userId = respuesta.userId,
                        descripcion = respuesta.descripcion,
                        fecha = respuesta.fecha,
                        estado = respuesta.estado,
                    )
                    respuestaList.add(CommentDto)
                }

                var PostResponseDto = PostResponseDto(
                    publicacionId = publicacion.publicacionId,
                    userId = publicacion.userId,
                    descripcion = publicacion.descripcion,
                    fecha = publicacion.fecha,
                    estado = publicacion.estado,
                    respuesta = respuestaList
                )
                listaPostResponseDto.add(PostResponseDto)
            }
            LOGGER.info("BL - Publicaciones obtenidas")
            return listaPostResponseDto
        } catch (e: Exception) {
            LOGGER.error("BL - Error al obtener las publicaciones de un usuario: ${e.message}")
            throw UcbException("Error al obtener las publicaciones de un usuario")
        }
    }
    
}