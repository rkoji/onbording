package com.onbording.auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RestApiException.class)
	protected ResponseEntity<ErrorResponse> handleRestApiException(RestApiException e) {
		ErrorCode errorCode = e.getErrorCode();
		return handleRestApiException(errorCode);
	}

	private ResponseEntity<ErrorResponse> handleRestApiException(ErrorCode errorCode) {
		return ResponseEntity
			.status(errorCode.getHttpStatus().value())
			.body(new ErrorResponse(errorCode));
	}
}
