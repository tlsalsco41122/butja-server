package com.dgsw.butja_server.global.exception

data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String
) {
    companion object {
        fun of(errorCode: CustomErrorCode) = ErrorResponse(
            status = errorCode.status.value(),
            code = errorCode.toString(),
            message = errorCode.message
        )
    }
}
