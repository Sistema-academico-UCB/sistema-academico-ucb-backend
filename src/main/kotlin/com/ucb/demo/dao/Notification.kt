package com.ucb.demo.dao

import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "notificacion")
@SequenceGenerator(name = "seq_notificacion", sequenceName = "seq_notificacion", allocationSize = 1)
class Notification (
    @Column(name = "usuario_id_usuario", nullable = false)
    var usuarioIdUsuario: Long,
    @Column(name = "emisor_id", nullable = false)
    var emisorId: Long,
    @Column(name = "mensaje", nullable = false)
    var mensaje: String,
    @Column(name = "fecha_envio", nullable = false)
    var fechaEnvio: Date,
    @Column(name = "estatus", nullable = false)
    var estatus: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notificacion")
    @Column(name = "notificacion_id")
    var notificacionId: Long = 0
) {
    constructor(): this(0, 0, "", Date(), true)
}