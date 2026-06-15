package com.dgsw.butja_server.domain.auth.controller.dto.req

data class SignUpReq(
    val username: String,
    val password: String,
    val nickname: String
)