package com.dgsw.butja_server.domain.job_application.domain

import com.dgsw.butja_server.domain.job_application.domain.enums.StageStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_stage")
class Stage (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    var jobApplication: JobApplication,

    @Column(nullable = false)
    var stageType: String,

    /** 말판에서의 순서 (0부터 시작) */
    @Column(nullable = false)
    val stageOrder: Int,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: StageStatus = if (stageOrder == 0) StageStatus.IN_PROGRESS else StageStatus.PENDING
        protected set

    // TODO: 일정 등록
}