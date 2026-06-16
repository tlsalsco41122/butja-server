package com.dgsw.butja_server.domain.auth.presentation.controller

import com.dgsw.butja_server.domain.auth.presentation.dto.req.SignInReq
import com.dgsw.butja_server.domain.auth.presentation.dto.req.SignUpReq
import com.dgsw.butja_server.domain.auth.presentation.dto.res.TokenRes
import com.dgsw.butja_server.domain.auth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody req: SignUpReq) {
        authService.signUp(req)
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    fun signIn(@RequestBody req: SignInReq): TokenRes {
        return authService.signIn(req)
    }
}