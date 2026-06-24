package com.dgsw.butja_server.domain.user.presentation.controller

import com.dgsw.butja_server.domain.user.presentation.dto.res.UserInfoRes
import com.dgsw.butja_server.domain.user.service.UserService
import com.dgsw.butja_server.global.common.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    fun getMyInfo(): ApiResponse<UserInfoRes> {
        return ApiResponse.success(userService.getMyInfo())
    }
}
