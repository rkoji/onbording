package com.onbording.auth.entity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SignRequestDto(
	@NotNull
	@Schema(description = "사용자 아이디" , example = "testId")
	String username,

	@NotNull
	@Schema(description = "비밀번호", example = "testpassword123")
	String password
) {
}
