package com.onbording.auth.entity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record RefreshTokenRequestDto (
	@Schema(description = "refreshToken" , example = "refreshToken123456")
	String refreshToken
){

}
