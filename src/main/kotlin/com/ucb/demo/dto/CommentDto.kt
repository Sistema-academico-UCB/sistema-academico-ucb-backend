package com.ucb.demo.dto

import java.util.Date

data class CommentDto(
    var respuestaId: Long,
    var userId: Long,
    var publicacionId: Long,
    var descripcion: String,
    var fecha: Date,
    var estado: Boolean
)