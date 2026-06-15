package com.dgsw.butja_server.global.exception

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.MethodNotAllowedException

@RestControllerAdvice
class CustomExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ErrorResponse {
        log.warn("CustomException: {}", e.error)
        return ErrorResponse.of(e.error)
    }

    @ExceptionHandler(MethodNotAllowedException::class)
    fun handleMethodNotAllowedException(e: MethodNotAllowedException): ErrorResponse =
        ErrorResponse.of(GlobalErrorCode.METHOD_NOT_ALLOWED)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ErrorResponse {
        log.error("Unhandled exception", e)
        return ErrorResponse.of(GlobalErrorCode.INTERNAL_SERVER_ERROR)
    }
}