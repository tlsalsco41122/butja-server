package com.dgsw.butja_server.domain.job_application.presentation.dto.res

data class ProgressRes(
    val totalCount: Int,
    val completedCount: Int,
    val progressRate: Double
)
