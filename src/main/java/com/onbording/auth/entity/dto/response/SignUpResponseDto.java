package com.onbording.auth.entity.dto.response;

import java.util.List;

import com.onbording.auth.entity.User;
import com.onbording.auth.entity.dto.request.SignUpRequestDto;

import lombok.Builder;

@Builder
public record SignUpResponseDto(
	String username,
	String nickname,
	List<AuthorityDto> authorities
) {

	public static SignUpResponseDto fromEntity(User user){

		List<AuthorityDto> authorityDtos = user.getRoles().stream()
			.map(AuthorityDto::fromRole)
			.toList();

		return SignUpResponseDto.builder()
			.username(user.getUsername())
			.nickname(user.getNickname())
			.authorities(authorityDtos)
			.build();
	}
}
