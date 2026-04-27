package com.dgsw.butja_server.global.exception

import org.springframework.http.HttpStatus

enum class GlobalError(override val status: HttpStatus, override val message: String) : CustomErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다.")
}