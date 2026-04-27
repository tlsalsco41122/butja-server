package com.dgsw.butja_server.global.security.jwt.util

import com.dgsw.butja_server.domain.auth.controller.dto.res.TokenRes
import com.dgsw.butja_server.global.security.jwt.JwtProperties
import com.dgsw.butja_server.global.security.jwt.enums.TokenType
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtProvider(
    private val jwtProperties: JwtProperties,
) {
    private val secretKey: SecretKey by lazy {
        val keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey)
        Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(username: String): TokenRes {
        return TokenRes(
            accessToken = generateTokenByType(username, TokenType.ACCESS, jwtProperties.accessExp),
            refreshToken = generateTokenByType(username, TokenType.REFRESH, jwtProperties.refreshExp)
        )
    }

    private fun generateTokenByType(username: String, tokenType: TokenType, expMillis: Long): String {
        val now = Date()
        return Jwts.builder()
            .header().type("JWT")
            .add("token_type", tokenType.name)
            .and()
            .issuer(jwtProperties.issuer)
            .issuedAt(now)
            .expiration(Date(now.time + expMillis))
            .subject(username)
            .signWith(secretKey)
            .compact()
    }
}