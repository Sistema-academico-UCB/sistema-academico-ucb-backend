package com.ucb.demo.dao

import com.ucb.demo.dto.TeacherDto
import javax.persistence.*

//Create table docente
@Entity
@Table(name = "docente")
@SequenceGenerator(name = "seq_docente", sequenceName = "seq_docente", allocationSize = 1)
class Teacher (
        @Column(name="tipo", nullable = false, length = 50)
        var tipo: String,
        @Column(name="user_id", nullable = false) //FK
        var userId: Long,
        @Column(name="estado", nullable = false)
        var estado: Boolean,
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_docente")
        @Column(name="docente_id", nullable = false)
        var docenteId: Long,


){
        constructor(): this("", 0, true, 0)
}