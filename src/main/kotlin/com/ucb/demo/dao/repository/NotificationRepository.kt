package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Notification
import org.springframework.data.repository.CrudRepository

interface NotificationRepository: CrudRepository<Notification, Long> {
    fun findByEmisorIdAndReceptorId(emisorId: Long, receptorId: Long): Notification?
    fun findAllByReceptorIdAndEstatus(receptorId: Long, estatus: Boolean): List<Notification>
}