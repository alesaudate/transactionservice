package com.alesaudate.transactionservice.interfaces.incoming.http

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class NotFoundHTTPStatusCode(message: String) : RuntimeException(message)

@ResponseStatus(BAD_REQUEST)
class BadRequestHTTPStatusCode(message: String) : RuntimeException(message)
