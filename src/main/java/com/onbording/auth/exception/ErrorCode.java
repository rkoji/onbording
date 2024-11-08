package com.onbording.auth.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	EXAMPLE_ERRORCODE(HttpStatus.NOT_FOUND,"예시 코드");
	private final HttpStatus httpStatus;
	private final String message;
}
