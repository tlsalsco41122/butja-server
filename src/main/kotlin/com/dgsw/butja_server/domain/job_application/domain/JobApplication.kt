package com.dgsw.butja_server.domain.job_application.domain

import com.dgsw.butja_server.domain.job_application.domain.enums.ApplicationStatus
import com.dgsw.butja_server.global.common.entity.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "tb_job_application")
class JobApplication(
    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    var companyName: String,

    @Column(nullable = false)
    var jobRole: String,

    @Column(nullable = false)
    val appliedDate: LocalDate

): BaseTimeEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(length = 2000)
    var memo: String? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ApplicationStatus = ApplicationStatus.ONGOING
        protected set

    fun updateStatus(status: ApplicationStatus) {
        this.status = status
    }

    fun updateMemo(memo: String?) {
        this.memo = memo
    }
}