package com.dgsw.butja_server.global.exception

class CustomException(val error: CustomErrorCode): RuntimeException(error.message) {
    val status: Int = error.status.value()
    val code: String = error.toString()
}