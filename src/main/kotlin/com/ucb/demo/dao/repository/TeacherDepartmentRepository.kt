package com.ucb.demo.dao.repository

import org.springframework.data.repository.CrudRepository
import com.ucb.demo.dao.TeacherDepartment

interface TeacherDepartmentRepository: CrudRepository<TeacherDepartment, Long> {
}