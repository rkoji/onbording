package com.onbording.auth.entity.dto.response;

public record TokenResponseDto(
	String accessToken,
	String refreshToken
) {
}
