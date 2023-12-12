package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Post
import org.springframework.data.repository.CrudRepository

interface PostRepository : CrudRepository<Post, Long> {
    fun findByUserIdAndEstadoIsTrue(userId: Long): List<Post>

}