package com.ucb.demo.dao.userRepository

import com.ucb.demo.dao.Student
import org.springframework.data.repository.CrudRepository

interface StudentRepository: CrudRepository<Student, Long> {

}