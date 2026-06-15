package com.dgsw.butja_server.domain.user.service

import com.dgsw.butja_server.domain.user.repository.UserRepository
import com.dgsw.butja_server.global.security.auth.UserSessionHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userSessionHolder: UserSessionHolder
) {
    @Transactional(readOnly = true)
    fun getMyInfo(): String {
        return userSessionHolder.getCurrentUser().username
    }

}