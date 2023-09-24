package com.ucb.demo.dao

import javax.persistence.*

@Entity
@Table(name = "departamento_carrera_docente")
@SequenceGenerator(name = "seq_departamento_carrera_docente", sequenceName = "seq_departamento_carrera_docente", allocationSize = 1)
class TeacherDepartment (
    @Column(name="docente_id", nullable = false)
    var docenteId: Long,
    @Column(name="departamento_carrera_id", nullable = false)
    var departamentoCarreraId: Long,
    @Column(name="director_carrera", nullable = false)
    var directorCarrera: Boolean,
    @Column(name="estado", nullable = false)
    var estado: Boolean,
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = "seq_departamento_carrera_docente")
    @Column(name = "departamento_carrera_docente_id")
    var departamentoCarreraDocenteId: Long = 0
    ){
    constructor(): this(0,0,false,true,0)
}