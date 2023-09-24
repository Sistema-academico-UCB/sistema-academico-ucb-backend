package com.ucb.demo.dao

import javax.persistence.*

@Entity
@Table(name = "colegio")
@SequenceGenerator(name = "seq_colegio", sequenceName = "seq_colegio", allocationSize = 1)
class College (
      @Column(name="nombre_colegio", nullable = false, length = 300)
      var nombreColegio: String,
      @Column(name="tipo", nullable = false, length = 50)
      var tipo: String,
      @Column(name="direccion", nullable = false, length = 300)
      var direccion: String,
      @Column(name="estado", nullable = false)
      var estado: Boolean,
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_colegio")
      @Column(name = "colegio_id")
      var colegioId: Long = 0
){
    constructor(): this("","","",true,0)
}
