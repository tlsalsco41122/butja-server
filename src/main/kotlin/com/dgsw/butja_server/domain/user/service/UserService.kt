package com.dgsw.butja_server.domain.user.service

import com.dgsw.butja_server.domain.auth.error.AuthErrorCode
import com.dgsw.butja_server.domain.user.presentation.dto.res.UserInfoRes
import com.dgsw.butja_server.global.exception.CustomException
import com.dgsw.butja_server.global.security.auth.UserSessionHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userSessionHolder: UserSessionHolder
) {
    @Transactional(readOnly = true)
    fun getMyInfo(): UserInfoRes {
        val user = userSessionHolder.getCurrentUser()

        return UserInfoRes(
            id = user.id ?: throw CustomException(AuthErrorCode.INVALID_CREDENTIALS),
            username = user.username,
            nickname = user.nickname
        )
    }
}
