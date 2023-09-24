package com.ucb.demo.dao.repository

import com.ucb.demo.dao.College
import org.springframework.data.repository.CrudRepository

interface CollegeRepository: CrudRepository<College, Long> {
}