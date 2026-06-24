package com.dgsw.butja_server.domain.job_application.error

import com.dgsw.butja_server.global.exception.CustomErrorCode
import org.springframework.http.HttpStatus

enum class JobApplicationErrorCode(override val status: HttpStatus, override val message: String): CustomErrorCode {
    JOB_APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "지원 내역을 찾을 수 없습니다."),
    STAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "채용 단계를 찾을 수 없습니다.")
}
