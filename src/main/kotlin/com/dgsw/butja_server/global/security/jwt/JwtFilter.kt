package com.dgsw.butja_server.global.security.jwt

import com.dgsw.butja_server.global.security.jwt.util.JwtExtractor
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtFilter (
    private val jwtExtractor: JwtExtractor
): OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = jwtExtractor.getToken(request)

        try {
            if (StringUtils.hasText(token)
                && jwtExtractor.validateToken(token!!)
                && SecurityContextHolder.getContext().authentication == null
            ) {
                val authentication = jwtExtractor.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
        }
        filterChain.doFilter(request, response)
    }
}