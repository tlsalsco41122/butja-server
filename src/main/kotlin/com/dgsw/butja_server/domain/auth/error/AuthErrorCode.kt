package com.dgsw.butja_server.domain.auth.error

import com.dgsw.butja_server.global.exception.CustomErrorCode
import org.springframework.http.HttpStatus

enum class AuthErrorCode(override val status: HttpStatus, override val message: String): CustomErrorCode {
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
}