package com.ucb.demo.dao.repository

import com.ucb.demo.dao.StudentCareer
import org.springframework.data.repository.CrudRepository

interface StudentCareerRepository: CrudRepository<StudentCareer, Long>{
}