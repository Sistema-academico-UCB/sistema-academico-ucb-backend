package com.ucb.demo.dao

import javax.persistence.*
import java.util.Date

@Entity
@Table(name = "publicacion")
@SequenceGenerator(name = "seq_publicacion", sequenceName = "seq_publicacion", allocationSize = 1)
class Post (
      @Column(name="user_id", nullable = false)
      var userId: Long,
      @Column(name="descripcion", nullable = false, length = 300) 
      var descripcion: String,
      @Column(name="fecha", nullable = false)
      var fecha: Date,
      @Column(name="estado", nullable = false)
      var estado: Boolean,
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_publicacion")
      @Column(name = "publicacion_id")
      var publicacionId: Long = 0
){
    constructor(): this(0,"",Date(),true,0)
}
