package com.dgsw.butja_server.domain.job_application.domain

import com.dgsw.butja_server.domain.user.domain.User
import com.dgsw.butja_server.global.common.entity.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "tb_job_application")
class JobApplication(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    var companyName: String,

    @Column(nullable = false)
    var jobRole: String,

    @Column(nullable = false)
    var appliedDate: LocalDate,

): BaseTimeEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(length = 2000)
    var memo: String? = null
        protected set

    fun updateMemo(memo: String?) {
        this.memo = memo
    }

    fun update(companyName: String?, jobRole: String?, appliedDate: LocalDate?, memo: String?) {
        companyName?.let { this.companyName = it }
        jobRole?.let { this.jobRole = it }
        appliedDate?.let { this.appliedDate = it }
        memo?.let { this.memo = it }
    }
}
