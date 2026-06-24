package com.dgsw.butja_server.domain.job_application.presentation.dto.req

import jakarta.validation.constraints.Min

data class UpdateStageOrderReq(
    @field:Min(value = 0, message = "단계 순서는 0 이상이어야 합니다.")
    val orderNumber: Int
)
