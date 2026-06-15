package com.dgsw.butja_server.global.security.auth

import com.dgsw.butja_server.domain.auth.error.AuthErrorCode
import com.dgsw.butja_server.domain.user.domain.User
import com.dgsw.butja_server.domain.user.repository.UserRepository
import com.dgsw.butja_server.global.exception.CustomException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserSessionHolder(
    private val userRepository: UserRepository
) {
    fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null || !authentication.isAuthenticated || authentication.principal == "anonymousUser") {
            throw CustomException(AuthErrorCode.USER_NOT_FOUND)
        }

        return userRepository.findByUsername(authentication.name)
            .orElseThrow { CustomException(AuthErrorCode.USER_NOT_FOUND) }
    }
}
