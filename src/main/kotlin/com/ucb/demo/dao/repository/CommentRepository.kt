package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository : CrudRepository<Comment, Long>{
    fun findByPublicacionIdAndEstadoIsTrue(publicacionId: Long): List<Comment>
}