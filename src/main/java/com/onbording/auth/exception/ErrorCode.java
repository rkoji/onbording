package com.onbording.auth.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "존재하는 아이디 입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 아이디입니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다."),
	PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "비밀번호는 필수 입력 항목입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
