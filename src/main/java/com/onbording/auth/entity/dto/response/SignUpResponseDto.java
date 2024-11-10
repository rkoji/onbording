package com.onbording.auth.entity.dto.response;

import java.util.List;

import com.onbording.auth.entity.User;
import com.onbording.auth.entity.dto.request.SignUpRequestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SignUpResponseDto(
	@Schema(description = "사용자 아이디" , example = "testId")
	String username,
	@Schema(description = "닉네임", example = "testNickname")
	String nickname,
	@Schema(description = "권한", example = "ROLE_USER,ROLE_ADMIN")
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
