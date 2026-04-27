package com.dgsw.butja_server.global.exception

import org.springframework.http.HttpStatus

interface CustomErrorCode {
    val status: HttpStatus
    val message: String
}