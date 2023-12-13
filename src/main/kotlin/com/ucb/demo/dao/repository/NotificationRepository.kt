package com.ucb.demo.dao.repository

import com.ucb.demo.dao.Notification
import org.springframework.data.repository.CrudRepository

interface NotificationRepository: CrudRepository<Notification, Long> {
    fun findByEmisorIdAndReceptorIdAndTipo(emisorId: Long, receptorId: Long, tipo: Int): Notification?
    fun findAllByReceptorIdAndEstatusAndTipo(receptorId: Long, estatus: Boolean, tipo: Int): List<Notification> ?
}