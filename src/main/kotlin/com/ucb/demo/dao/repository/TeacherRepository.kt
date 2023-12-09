package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Teacher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface TeacherRepository: CrudRepository<Teacher, Long> {
    fun existsByUserIdAndEstado(userId: Long, estado: Boolean): Boolean

    //Obtener el registro de docente por id de usuario
    fun findByUserIdAndEstado(userId: Long, estado: Boolean): Teacher?

    @Query(
            value = "SELECT " +
                    "d.docente_id AS docente_id, " +
                    "d.tipo AS tipo, " +
                    "d.user_id AS user_id, " +
                    "d.estado AS estado " +
                    "FROM persona p " +
                    "JOIN usuario u ON p.persona_id = u.persona_id " +
                    "JOIN docente d ON u.user_id = d.user_id " +
                    "JOIN departamento_carrera_docente dpd ON d.docente_id = dpd.docente_id " +
                    "JOIN departamento_carrera de ON dpd.departamento_carrera_id = de.departamento_carrera_id " +
                    "JOIN docente_profesion dp ON d.docente_id = dp.docente_id " +
                    "JOIN profesion pr ON dp.profesion_id = pr.profesion_id " +
                    "WHERE " +
                    "(:carnetIdentidad IS NULL OR p.carnet_identidad ILIKE CONCAT('%', :carnetIdentidad, '%')) " +
                    "AND u.estado = TRUE " +
                    "AND d.estado = TRUE " +
                    "AND p.estado = TRUE " +
                    "AND dp.estado = TRUE " +
                    "AND dpd.estado = TRUE " +
                    "AND (:departamentoId IS NULL OR de.departamento_carrera_id = :departamentoId) " +
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
    fun filtrarDocentes(
            @Param("carnetIdentidad") carnetIdentidad: String?,
            @Param("departamentoId") departamentoId: Long?,
            @Param("nombre") nombre: String?,
            @Param("sortBy") sortBy: String?,
            @Param("sortType") sortType: String?,
            pageable: Pageable
    ): Page<Teacher>


}