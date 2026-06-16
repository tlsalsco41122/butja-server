package com.dgsw.butja_server.domain.auth.presentation.dto.req

data class SignInReq(
    val username: String,
    val password: String
)