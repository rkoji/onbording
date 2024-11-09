package com.onbording.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
	private final ErrorCode errorCode;

	@Override
	public String getMessage() {
		return errorCode.getMessage();
	}
}
