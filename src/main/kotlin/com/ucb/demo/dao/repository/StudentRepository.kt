package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Student
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface StudentRepository: CrudRepository<Student, Long> {
    fun existsByUserIdAndEstado(userId: Long, estado: Boolean): Boolean

    fun findByUserIdAndEstado(userId: Long, estado: Boolean): Student?

    @Query(
            value = "SELECT " +
                    "e.estudiante_id AS estudiante_id, " +
                    "e.semestre AS semestre, " +
                    "e.tipo AS tipo, " +
                    "e.user_id AS user_id, " +
                    "e.colegio_id AS colegio_id, " +
                    "e.estado AS estado " +
                    "FROM persona p " +
                    "JOIN usuario u ON p.persona_id = u.persona_id " +
                    "JOIN estudiante e ON u.user_id = e.user_id " +
                    "JOIN carrera_estudiante ce ON e.estudiante_id = ce.estudiante_id " +
                    "WHERE " +
                    "(:carnetIdentidad IS NULL OR p.carnet_identidad ILIKE CONCAT('%', :carnetIdentidad, '%')) " +
                    "AND u.estado = TRUE " +
                    "AND e.estado = TRUE " +
                    "AND p.estado = TRUE " +
                    "AND (:semestre IS NULL OR e.semestre = :semestre) " +
                    "AND (:carreraId IS NULL OR ce.carrera_id = :carreraId) " +
                    "AND (:nombre IS NULL OR CONCAT(p.nombre, ' ', p.apellido_paterno, ' ', p.apellido_materno) ILIKE CONCAT('%', :nombre, '%')) " +
                    "ORDER BY " +
                    "CASE " +
                    "  WHEN :sortBy = 'apellido_paterno' AND :sortType = 'asc' THEN p.apellido_paterno " +
                    "  WHEN :sortBy = 'carnet_identidad' AND :sortType = 'asc' THEN p.carnet_identidad " +
                    "END ASC, " +
                    "CASE " +
                    "  WHEN :sortBy = 'apellido_paterno' AND :sortType = 'desc' THEN p.apellido_paterno " +
                    "  WHEN :sortBy = 'carnet_identidad' AND :sortType = 'desc' THEN p.carnet_identidad " +
                    "END DESC",
            nativeQuery = true
    )
    fun filtrarEstudiantes(
            @Param("carnetIdentidad") carnetIdentidad: String?,
            @Param("semestre") semestre: Int?,
            @Param("carreraId") carreraId: Long?,
            @Param("nombre") nombre: String?,
            @Param("sortBy") sortBy: String?,
            @Param("sortType") sortType: String?,
            pageable: Pageable
    ): Page<Student>

}