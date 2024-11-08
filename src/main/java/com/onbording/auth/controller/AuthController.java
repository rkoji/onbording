package com.onbording.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onbording.auth.entity.dto.request.SignUpRequestDto;
import com.onbording.auth.entity.dto.response.SignUpResponseDto;
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
}
