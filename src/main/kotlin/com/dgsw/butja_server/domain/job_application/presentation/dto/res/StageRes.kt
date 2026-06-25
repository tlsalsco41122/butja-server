package com.dgsw.butja_server.domain.job_application.presentation.dto.res

import com.dgsw.butja_server.domain.job_application.domain.enums.StageStatus
import java.time.LocalDateTime

data class StageRes(
    val id: Long,
    val jobApplicationId: Long,
    val name: String,
    val orderNumber: Int,
    val status: StageStatus,
    val scheduledAt: LocalDateTime?,
    val memo: String?
)
