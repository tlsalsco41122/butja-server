package com.dgsw.butja_server.domain.job_application.presentation.dto.req

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateStageReq(
    @field:NotBlank(message = "단계명을 입력해주세요.")
    @field:Size(max = 100, message = "단계명은 100자 이하여야 합니다.")
    val name: String,

    @field:Min(value = 0, message = "단계 순서는 0 이상이어야 합니다.")
    val orderNumber: Int?,

    val scheduledAt: LocalDateTime?,

    @field:Size(max = 2000, message = "메모는 2000자 이하여야 합니다.")
    val memo: String?
)
