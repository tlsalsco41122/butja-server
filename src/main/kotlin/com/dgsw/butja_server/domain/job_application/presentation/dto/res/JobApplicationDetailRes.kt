package com.dgsw.butja_server.domain.job_application.presentation.dto.res

import java.time.LocalDate
import java.time.LocalDateTime

data class JobApplicationDetailRes(
    val id: Long,
    val companyName: String,
    val jobRole: String,
    val appliedDate: LocalDate,
    val memo: String?,
    val currentStage: StageRes?,
    val progress: ProgressRes,
    val stages: List<StageRes>,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
