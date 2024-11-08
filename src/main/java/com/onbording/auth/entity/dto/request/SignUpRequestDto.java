package com.onbording.auth.entity.dto.request;

import jakarta.validation.constraints.NotNull;

public record SignUpRequestDto(
	@NotNull String username,
	@NotNull String password,
	@NotNull String nickname
) {

}
