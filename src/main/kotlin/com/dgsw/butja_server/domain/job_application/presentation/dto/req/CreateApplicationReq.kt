package com.dgsw.butja_server.domain.job_application.presentation.dto.req

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class CreateApplicationReq(
    @field:NotBlank(message = "회사명을 입력해주세요.")
    @field:Size(max = 100, message = "회사명은 100자 이하여야 합니다.")
    val companyName: String,

    @field:NotBlank(message = "직무를 입력해주세요.")
    @field:Size(max = 100, message = "직무는 100자 이하여야 합니다.")
    val jobRole: String,

    @field:NotNull(message = "지원일을 입력해주세요.")
    val appliedDate: LocalDate,

    @field:Size(max = 2000, message = "메모는 2000자 이하여야 합니다.")
    val memo: String?
)
