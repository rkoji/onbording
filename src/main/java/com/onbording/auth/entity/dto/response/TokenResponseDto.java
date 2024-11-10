package com.onbording.auth.entity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponseDto(
	@Schema(description = "accessToken" , example = "accessToken123456")
	String accessToken,
	@Schema(description = "refreshToken" , example = "refreshToken123456")
	String refreshToken
) {
}
