package com.dgsw.butja_server.global.exception

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.MethodNotAllowedException

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ErrorResponse =
        ErrorResponse.of(e.error)

    @ExceptionHandler(MethodNotAllowedException::class)
    fun handleMethodNotAllowedException(e: MethodNotAllowedException): ErrorResponse =
        ErrorResponse.of(GlobalError.METHOD_NOT_ALLOWED)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ErrorResponse =
        ErrorResponse.of(GlobalError.INTERNAL_SERVER_ERROR)
}