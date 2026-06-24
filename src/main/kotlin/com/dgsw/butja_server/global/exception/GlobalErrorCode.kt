package com.dgsw.butja_server.global.exception

import org.springframework.http.HttpStatus

enum class GlobalErrorCode(override val status: HttpStatus, override val message: String): CustomErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다.")
}
