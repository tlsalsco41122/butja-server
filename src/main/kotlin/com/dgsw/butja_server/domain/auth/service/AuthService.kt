package com.dgsw.butja_server.domain.auth.service

import com.dgsw.butja_server.domain.auth.presentation.dto.req.SignInReq
import com.dgsw.butja_server.domain.auth.presentation.dto.req.SignUpReq
import com.dgsw.butja_server.domain.auth.presentation.dto.res.TokenRes
import com.dgsw.butja_server.domain.auth.error.AuthErrorCode
import com.dgsw.butja_server.domain.user.domain.User
import com.dgsw.butja_server.domain.user.repository.UserRepository
import com.dgsw.butja_server.global.exception.CustomException
import com.dgsw.butja_server.global.security.jwt.util.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun signUp(req: SignUpReq) {
        if (userRepository.findByUsername(req.username).isPresent) {
            throw CustomException(AuthErrorCode.USERNAME_ALREADY_EXISTS)
        }

        val user = User(
            username = req.username,
            password = passwordEncoder.encode(req.password),
            nickname = req.nickname
        )

        userRepository.save(user)
    }

    fun signIn(req: SignInReq): TokenRes {
        val user = userRepository.findByUsername(req.username)
            .orElseThrow { CustomException(AuthErrorCode.INVALID_CREDENTIALS) }

        if (!passwordEncoder.matches(req.password, user.password)) {
            throw CustomException(AuthErrorCode.INVALID_CREDENTIALS)
        }

        return jwtProvider.generateToken(user.username)
    }

}