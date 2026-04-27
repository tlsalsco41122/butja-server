package com.dgsw.butja_server.global.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val issuer: String,
    val secretKey: String,
    val accessExp: Long,
    val refreshExp: Long
)
