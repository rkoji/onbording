package com.onbording.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onbording.auth.entity.dto.request.RefreshToeknRequestDto;
import com.onbording.auth.entity.dto.request.RefreshTokenRequestDto;
import com.onbording.auth.entity.dto.request.SignRequestDto;
import com.onbording.auth.entity.dto.request.SignUpRequestDto;
import com.onbording.auth.entity.dto.response.SignUpResponseDto;
import com.onbording.auth.entity.dto.response.TokenResponseDto;
import com.onbording.auth.jwt.JWTUtil;
import com.onbording.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto dto){
		return ResponseEntity.ok().body(authService.signUp(dto));
	}

	// 로그인
	@PostMapping("/sign")
	public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid SignRequestDto dto){
		TokenResponseDto responseDto = authService.login(dto);
		return ResponseEntity.ok()
			.header("Authorization","Bearer "+ responseDto.accessToken())
			.body(responseDto);
	}

	@PostMapping("/refresh")
	public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto requestDto){
		TokenResponseDto responseDto = authService.refreshAccessToken(requestDto);
		return ResponseEntity.ok()
			.header("Authorization","Bearer "+ responseDto.accessToken())
			.body(responseDto);
	}
}
