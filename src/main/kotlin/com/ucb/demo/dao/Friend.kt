package com.ucb.demo.dao

import javax.persistence.*

@Entity
@Table(name = "amigo")
@SequenceGenerator(name = "seq_amigo", sequenceName = "seq_amigo", allocationSize = 1)
class Friend (
    @Column(name = "usuario_id_usuario", nullable = false)
    var usuarioIdUsuario: Long,
    @Column(name = "amigo_id_usuario", nullable = false)
    var amigoIdUsuario: Long,
    @Column(name = "aceptado", nullable = false)
    var aceptado: Boolean = false,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_amigo")
    @Column(name = "amigo_id")
    var amigoId: Long = 0
){
    constructor(): this(0, 0)
}