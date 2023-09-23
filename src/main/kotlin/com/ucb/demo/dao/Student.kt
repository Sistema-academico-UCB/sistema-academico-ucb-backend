package com.ucb.demo.dao

import javax.persistence.*

//Create table estudiante
@Entity
@Table(name = "estudiante")
@SequenceGenerator(name = "seq_estudiante", sequenceName = "seq_estudiante", allocationSize = 1)
class Student (
        @Column(name="semestre", nullable = false)
        var semestre: Int,
        @Column(name="tipo", nullable = false, length = 50)
        var tipo: String,
        @Column(name="user_id", nullable = false) //FK
        var userId: Long,
        @Column(name="colegio_id", nullable = false) //FK
        var colegioId: Long,
        @Column(name="estado", nullable = false)
        var estado: Boolean,
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_estudiante")
        @Column(name="estudiante_id", nullable = false)
        var estudianteId: Long
){
    constructor(): this(1,"",0,0,true,0)
}
