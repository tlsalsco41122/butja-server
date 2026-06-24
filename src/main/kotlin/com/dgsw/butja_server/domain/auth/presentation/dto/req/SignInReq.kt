package com.dgsw.butja_server.domain.auth.presentation.dto.req

import jakarta.validation.constraints.NotBlank

data class SignInReq(
    @field:NotBlank(message = "아이디를 입력해주세요.")
    val username: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    val password: String
)
