package com.ucb.demo.dao

import javax.persistence.*

@Entity
@Table(name = "docente_profesion")
@SequenceGenerator(name = "seq_docente_profesion", sequenceName = "seq_docente_profesion", allocationSize = 1)
class TeacherProfession (
        @Column(name="profesion_id", nullable = false)
        var profesionId: Long,
        @Column(name="docente_id", nullable = false)
        var docenteId: Long,
        @Id
        @GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = "seq_docente_profesion")
        @Column(name = "docente_profesion_id")
        var docenteProfesionId: Long = 0,

        @ManyToOne
        @JoinColumn(name = "docente_id", insertable = false, updatable = false)
        var docente: Teacher? = null,

        @ManyToOne
        @JoinColumn(name = "docente_profesion_id", insertable = false, updatable = false)
        var profesion: Profession? = null,
){
    constructor(): this(0,0,0)
}
