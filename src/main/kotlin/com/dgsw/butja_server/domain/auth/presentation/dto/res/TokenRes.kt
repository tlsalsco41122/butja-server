package com.dgsw.butja_server.domain.auth.presentation.dto.res

data class TokenRes(
    val accessToken: String,
    val refreshToken: String
)
