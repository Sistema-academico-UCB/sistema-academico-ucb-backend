package com.ucb.demo.dao

import javax.persistence.*

//Create table usuario
@Entity
@Table(name = "usuario")
@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1)
class User (
    @Column(name = "username", nullable = false, length = 50)
    var username: String,
    @Column(name = "secret", nullable = false, length = 150)
    var secret: String,
    @Column(name = "rol", nullable = false, length = 50)
    var rol: String,
    @Column(name = "persona_id", nullable = false)
    var personaId: Long,
    @Column(name = "estado", nullable = false)
    var estado: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @Column(name = "user_id")
    var userId: Long = 0,

    @OneToOne
    @JoinColumn(name = "persona_id", insertable = false, updatable = false)
    var persona: Persona? = null,

    @OneToOne(mappedBy = "usuario")
    var estudiante: Student? = null
    ){
    constructor(): this("", "", "", 0, true)
}
