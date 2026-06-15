package com.dgsw.butja_server.global.security.jwt.util

import com.dgsw.butja_server.domain.user.repository.UserRepository
import com.dgsw.butja_server.global.exception.CustomException
import com.dgsw.butja_server.global.security.auth.AuthDetails
import com.dgsw.butja_server.global.security.jwt.JwtProperties
import com.dgsw.butja_server.global.security.jwt.enums.TokenType
import com.dgsw.butja_server.global.security.jwt.error.JwtErrorCode
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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

    fun getToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization") ?: return null
        if (!authHeader.startsWith("Bearer ")) return null
        return authHeader.substring(7)
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token).payload
        val username = claims.subject.trim()
        val user = userRepository.findByUsername(username)
            .orElseThrow { CustomException(JwtErrorCode.INVALID_TOKEN) }

        val authDetails = AuthDetails(user)
        return UsernamePasswordAuthenticationToken(authDetails, null, authDetails.authorities)
    }

    fun isWrongType(token: String, tokenType: TokenType): Boolean {
        val claims = getClaims(token)
        val type = claims.header["token_type"] as? String

        return type != tokenType.name
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            return true
        } catch (e: JwtException ) {
            return false
        } catch (e: IllegalArgumentException) {
            return false
        }
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