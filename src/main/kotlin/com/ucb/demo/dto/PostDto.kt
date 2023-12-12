package com.ucb.demo.dto

import java.util.Date

data class PostDto(
    var publicacionId: Long,
    var userId: Long,
    var descripcion: String,
    var fecha: Date,
    var estado: Boolean,
)