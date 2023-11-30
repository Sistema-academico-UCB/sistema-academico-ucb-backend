package com.ucb.demo.dao

import javax.persistence.*

@Entity
@Table(name = "departamento_carrera")
@SequenceGenerator(name = "seq_carrera", sequenceName = "seq_carrera", allocationSize = 1, initialValue = 26)
class Career (
    @Column(name="sigla", nullable = false, length = 50)
    var sigla: String,
    @Column(name="nombre", nullable = false, length = 50)
    var nombre: String,
    @Column(name="programa", nullable = false, length = 50)
    var programa: String,
    @Column(name="carrera", nullable = false)
    var carrera: Boolean,
    @Column(name="estado", nullable = false)
    var estado: Boolean,
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = "seq_carrera")
    @Column(name = "departamento_carrera_id")
    var departamentoCarreraId: Long = 0,

    @OneToMany(mappedBy = "carrera")
    var carrerasEstudiante: List<StudentCareer> = ArrayList()
)
{
    constructor(): this("","","",false,true)
}