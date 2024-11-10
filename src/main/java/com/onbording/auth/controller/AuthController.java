package com.onbording.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onbording.auth.entity.dto.request.RefreshTokenRequestDto;
import com.onbording.auth.entity.dto.request.SignRequestDto;
import com.onbording.auth.entity.dto.request.SignUpRequestDto;
import com.onbording.auth.entity.dto.response.SignUpResponseDto;
import com.onbording.auth.entity.dto.response.TokenResponseDto;
import com.onbording.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth",description = "Auth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	@Operation(summary = "SingUp API",description = "회원가입 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200",description = "회원가입 성공",
		content = {@Content(schema = @Schema(implementation = SignUpResponseDto.class))}),
		@ApiResponse(responseCode = "409", description ="존재하는 아이디 입니다."),
		@ApiResponse(responseCode = "400",description = "비밀번호는 필수 입력 항목입니다.")
	})
	public ResponseEntity<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto dto){
		return ResponseEntity.ok().body(authService.signUp(dto));
	}

	@PostMapping("/sign")
	@Operation(summary = "SingIn API",description = "로그인 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200",description = "로그인 성공",
		content = {@Content(schema = @Schema(implementation = TokenResponseDto.class))}),
		@ApiResponse(responseCode = "404",description = "존재하지 않는 아이디입니다."),
		@ApiResponse(responseCode ="401",description = "비밀번호가 일치하지 않습니다.")
	})
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
