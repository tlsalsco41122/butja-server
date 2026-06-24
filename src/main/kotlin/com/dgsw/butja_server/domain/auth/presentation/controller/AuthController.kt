package com.dgsw.butja_server.domain.auth.presentation.controller

import com.dgsw.butja_server.domain.auth.presentation.dto.req.SignInReq
import com.dgsw.butja_server.domain.auth.presentation.dto.req.SignUpReq
import com.dgsw.butja_server.domain.auth.presentation.dto.res.TokenRes
import com.dgsw.butja_server.domain.auth.service.AuthService
import com.dgsw.butja_server.global.common.response.ApiResponse
import jakarta.validation.Valid
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
    fun signUp(@Valid @RequestBody req: SignUpReq): ApiResponse<Nothing> {
        authService.signUp(req)
        return ApiResponse.success(null)
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    fun signIn(@Valid @RequestBody req: SignInReq): ApiResponse<TokenRes> {
        return ApiResponse.success(authService.signIn(req))
    }
}
