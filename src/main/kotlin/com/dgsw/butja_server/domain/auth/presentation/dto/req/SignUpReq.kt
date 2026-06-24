package com.dgsw.butja_server.domain.auth.presentation.dto.req

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignUpReq(
    @field:NotBlank(message = "아이디를 입력해주세요.")
    @field:Size(max = 50, message = "아이디는 50자 이하여야 합니다.")
    val username: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    @field:Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하여야 합니다.")
    val password: String,

    @field:NotBlank(message = "닉네임을 입력해주세요.")
    @field:Size(max = 50, message = "닉네임은 50자 이하여야 합니다.")
    val nickname: String
)
