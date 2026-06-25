package com.dgsw.butja_server.domain.job_application.domain

import com.dgsw.butja_server.domain.job_application.domain.enums.StageStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_stage")
class Stage(
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: StageStatus = StageStatus.PENDING
        protected set

    // 기존 completed 필드는 status로 대체되므로 제거하거나 하위 호환용으로 유지
    // 여기서는 status 기반으로 완전히 전환하는 것을 권장
    @get:Transient
    val completed: Boolean
        get() = status == StageStatus.COMPLETED

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
        this.status = if (completed) StageStatus.COMPLETED else StageStatus.PENDING
    }

    fun markFailed() {
        this.status = StageStatus.FAILED
    }

    fun markInProgress() {
        this.status = StageStatus.IN_PROGRESS
    }
}