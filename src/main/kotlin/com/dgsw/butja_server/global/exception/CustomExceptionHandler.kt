package com.dgsw.butja_server.global.exception

import com.dgsw.butja_server.global.common.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ApiResponse<Nothing>> {
        log.warn("CustomException: {}", e.error)
        return ResponseEntity
            .status(e.error.status)
            .body(ApiResponse.fail(e.error.message))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val message = e.bindingResult.fieldErrors.firstOrNull()?.defaultMessage
            ?: GlobalErrorCode.BAD_REQUEST.message

        return ResponseEntity
            .status(GlobalErrorCode.BAD_REQUEST.status)
            .body(ApiResponse.fail(message))
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotAllowedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(GlobalErrorCode.METHOD_NOT_ALLOWED.status)
            .body(ApiResponse.fail(GlobalErrorCode.METHOD_NOT_ALLOWED.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Nothing>> {
        log.error("Unhandled exception", e)
        return ResponseEntity
            .status(GlobalErrorCode.INTERNAL_SERVER_ERROR.status)
            .body(ApiResponse.fail(GlobalErrorCode.INTERNAL_SERVER_ERROR.message))
    }
}
