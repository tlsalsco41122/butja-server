package com.dgsw.butja_server.domain.job_application.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_stage")
class Stage (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    var jobApplication: JobApplication,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var orderNumber: Int,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(nullable = false)
    var completed: Boolean = false
        protected set

    var scheduledAt: LocalDateTime? = null
        protected set

    @Column(length = 2000)
    var memo: String? = null
        protected set

    fun update(name: String?, scheduledAt: LocalDateTime?, memo: String?) {
        name?.let { this.name = it }
        scheduledAt?.let { this.scheduledAt = it }
        memo?.let { this.memo = it }
    }

    fun updateOrderNumber(orderNumber: Int) {
        this.orderNumber = orderNumber
    }

    fun updateCompleted(completed: Boolean) {
        this.completed = completed
    }
}
