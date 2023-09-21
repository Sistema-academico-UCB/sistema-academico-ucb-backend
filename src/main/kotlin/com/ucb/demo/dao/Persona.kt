package com.ucb.demo.dao

import java.util.Date
import javax.persistence.*

//Create table persona
@Entity
@Table(name = "persona")
@SequenceGenerator(name = "seq_persona", sequenceName = "seq_persona", allocationSize = 1)
class Persona (
    @Column(name = "nombre", nullable = false, length = 150)
    var nombre: String,
    @Column(name = "apellido_paterno", nullable = false, length = 100)
    var apellidoPaterno: String,
    @Column(name = "apellido_materno", nullable = false, length = 100)
    var apellidoMaterno: String,
    @Column(name = "carnet_identidad", nullable = false, length = 50)
    var carnetIdentidad: String,
    @Column(name = "fecha_nacimiento", nullable = false)
    var fechaNacimiento: Date,
    @Column(name = "correo", nullable = false, length = 150)
    var correo: String,
    @Column(name = "genero", nullable = false, length = 50)
    var genero: String,
    @Column(name = "celular", nullable = false, length = 50)
    var celular: String,
    @Column(name = "descripcion", nullable = false, length = 400)
    var descripcion: String,
    @Column(name = "uuid_foto", nullable = false, length = 150)
    var uuidFoto: String,
    @Column(name = "direccion", nullable = false, length = 150)
    var direccion: String,
    @Column(name = "fecha_registro", nullable = false)
    var fechaRegistro: Date,
    @Column(name = "estado_civil", nullable = false, length = 50)
    var estadoCivil: String,
    @Column(name = "estado", nullable = false)
    var estado: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_persona")
    @Column(name = "persona_id")
    var idPersona: Long = 0
    ){
    constructor(): this("", "", "", "", Date(), "", "", "", "", "", "", Date(), "", true)

}