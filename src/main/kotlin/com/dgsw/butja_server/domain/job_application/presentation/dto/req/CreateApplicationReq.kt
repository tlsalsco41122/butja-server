package com.dgsw.butja_server.domain.job_application.presentation.dto.req

import java.time.LocalDate

data class CreateApplicationReq(
    val companyName: String,
    val jobRole: String?,
    val appliedDate: LocalDate,
    val memo: String?
)
