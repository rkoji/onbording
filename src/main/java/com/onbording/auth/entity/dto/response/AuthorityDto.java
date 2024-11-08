package com.onbording.auth.entity.dto.response;

import com.onbording.auth.entity.UserRole;

public record AuthorityDto(
	String authorityName
) {
	public static AuthorityDto fromRole(UserRole role) {
		return new AuthorityDto(role.name());
	}
}
