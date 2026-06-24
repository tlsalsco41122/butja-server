package com.dgsw.butja_server.global.common.response

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String
) {
    companion object {
        fun <T> success(data: T?, message: String = "success"): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
                message = message
            )
        }

        fun fail(message: String): ApiResponse<Nothing> {
            return ApiResponse(
                success = false,
                data = null,
                message = message
            )
        }
    }
}
