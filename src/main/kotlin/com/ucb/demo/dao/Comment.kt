package com.ucb.demo.dao

import java.util.Date

import javax.persistence.*

@Entity
@Table(name = "respuesta")
@SequenceGenerator(name = "seq_respuesta", sequenceName = "seq_respuesta", allocationSize = 1)
class Comment (
      @Column(name="user_id", nullable = false)
      var userId: Long,
      @Column(name="publicacion_id", nullable = false)
      var publicacionId: Long,
      @Column(name="descripcion", nullable = false, length = 300) 
      var descripcion: String,
      @Column(name="fecha", nullable = false)
      var fecha: Date,
      @Column(name="estado", nullable = false)
      var estado: Boolean,
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_respuesta")
      @Column(name = "respuesta_id")
      var respuestaId: Long = 0
){
    constructor(): this(0,0,"",Date(),true,0)
}
