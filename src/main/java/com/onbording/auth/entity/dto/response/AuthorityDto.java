package com.onbording.auth.entity.dto.response;

import com.onbording.auth.entity.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthorityDto(

	@Schema(description = "권한", example = "ROLE_USER")
	String authorityName
) {
	public static AuthorityDto fromRole(UserRole role) {
		return new AuthorityDto(role.name());
	}
}
