package com.dgsw.butja_server.domain.job_application.presentation.dto.req

import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class UpdateStageReq(
    @field:Size(max = 100, message = "단계명은 100자 이하여야 합니다.")
    val name: String?,

    val scheduledAt: LocalDateTime?,

    @field:Size(max = 2000, message = "메모는 2000자 이하여야 합니다.")
    val memo: String?
)
