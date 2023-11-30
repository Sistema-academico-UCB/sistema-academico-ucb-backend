package com.ucb.demo.dao

import javax.persistence.*

@Entity
@Table(name = "profesion")
@SequenceGenerator(name = "seq_profesion", sequenceName = "seq_profesion", allocationSize = 1)
class Profession (
        @Column(name="nombre_profesion", nullable = false, length = 150)
        var nombreProfesion: String,
        @Column(name="estado", nullable = false)
        var estado: Boolean,
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_profesion")
        @Column(name = "profesion_id")
        var profesionId: Long = 0,

        @OneToMany(mappedBy = "docente")
        var docentesProfesion: List<TeacherProfession> = ArrayList()
){
    constructor(): this("",true,0)
}