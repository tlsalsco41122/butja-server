package com.dgsw.butja_server.global.security.jwt.util

import com.dgsw.butja_server.domain.user.repository.UserRepository
import com.dgsw.butja_server.global.exception.CustomException
import com.dgsw.butja_server.global.security.jwt.JwtProperties
import com.dgsw.butja_server.global.security.jwt.enums.TokenType
import com.dgsw.butja_server.global.security.jwt.error.JwtErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class JwtExtractor(
    private val jwtProperties: JwtProperties,
    private val userRepository: UserRepository
) {
    private val secretKey: SecretKey by lazy {
        val keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey)
        Keys.hmacShaKeyFor(keyBytes)
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token).payload
        val subject = claims.subject.trim()
        val
    }

    fun isWrongType(token: String, tokenType: TokenType): Boolean {
        val claims = getClaims(token)
        val type = claims.header.type

        return type != tokenType.toString()
    }

    private fun getClaims(token: String): Jws<Claims> {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
        } catch (e: ExpiredJwtException) {
            throw CustomException(JwtErrorCode.EXPIRED_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw CustomException(JwtErrorCode.INVALID_TOKEN)
        } catch (e: MalformedJwtException) {
            throw CustomException(JwtErrorCode.MALFORMED_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw CustomException(JwtErrorCode.UNSUPPORTED_TOKEN)
        }
    }
}