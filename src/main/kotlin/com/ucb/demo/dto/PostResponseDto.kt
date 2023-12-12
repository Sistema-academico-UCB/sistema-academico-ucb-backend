package com.ucb.demo.dto

import java.util.Date
import com.ucb.demo.dto.CommentDto

data class PostResponseDto(
    var publicacionId: Long,
    var userId: Long,
    var descripcion: String,
    var fecha: Date,
    var estado: Boolean,
    var respuesta: List<CommentDto>
)