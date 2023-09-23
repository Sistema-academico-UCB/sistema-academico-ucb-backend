package com.ucb.demo.dao.userRepository

import com.ucb.demo.dao.Teacher
import org.springframework.data.repository.CrudRepository

interface TeacherRepository: CrudRepository<Teacher, Long> {
}