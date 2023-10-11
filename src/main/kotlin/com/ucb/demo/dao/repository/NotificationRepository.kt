package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Notification
import org.springframework.data.repository.CrudRepository

interface NotificationRepository: CrudRepository<Notification, Long> {
}