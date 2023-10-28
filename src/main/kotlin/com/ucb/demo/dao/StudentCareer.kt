package com.ucb.demo.dao

import javax.persistence.*

@Entity
@Table(name = "carrera_estudiante")
@SequenceGenerator(name = "seq_carrera_estudiante", sequenceName = "seq_carrera_estudiante", allocationSize = 1)
class StudentCareer(
        @Column(name="estudiante_id", nullable = false)
        var estudianteId: Long,
        @Column(name="carrera_id", nullable = false)
        var carreraId: Long,
        @Column(name="periodo_academico_id", nullable = false)
        var periodoAcademicoId: Long,
        @Column(name="estado", nullable = false)
        var estado: Boolean,
        @Id
        @GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = "seq_carrera_estudiante")
        @Column(name = "carrera_estudiante_id")
        var carreraEstudianteId: Long = 0,

        @ManyToOne
        @JoinColumn(name = "estudiante_id", insertable = false, updatable = false)
        var estudiante: Student? = null,

        @ManyToOne
        @JoinColumn(name = "carrera_id", insertable = false, updatable = false)
        var carrera: Career? = null,
)
{
    constructor(): this(0,0,0,true,0)
}