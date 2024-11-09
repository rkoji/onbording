package com.onbording.auth.service;

import static com.onbording.auth.exception.ErrorCode.*;

import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onbording.auth.entity.User;
import com.onbording.auth.entity.UserRole;
import com.onbording.auth.entity.dto.CustomUserDetails;
import com.onbording.auth.entity.dto.request.RefreshTokenRequestDto;
import com.onbording.auth.entity.dto.request.SignRequestDto;
import com.onbording.auth.entity.dto.request.SignUpRequestDto;
import com.onbording.auth.entity.dto.response.SignUpResponseDto;
import com.onbording.auth.entity.dto.response.TokenResponseDto;
import com.onbording.auth.exception.RestApiException;
import com.onbording.auth.jwt.JWTUtil;
import com.onbording.auth.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JWTUtil jwtUtil;

	// 회원 가입
	public SignUpResponseDto signUp(SignUpRequestDto dto) {
		if (userRepository.existsByUsername(dto.username())){
			throw new RestApiException(USERNAME_ALREADY_EXISTS);
		}

		if (dto.password() == null || dto.password().isBlank()){
			throw new RestApiException(PASSWORD_REQUIRED);
		}

		String encodedPassword = passwordEncoder.encode(dto.password());

		User user = User.signup(dto,encodedPassword);
		userRepository.save(user);

		return SignUpResponseDto.fromEntity(user);
	}

	public TokenResponseDto login(@Valid SignRequestDto dto) {
		User user = userRepository.findByUsername(dto.username()).orElseThrow(
			() -> new RestApiException(USER_NOT_FOUND)
		);

		if (!passwordEncoder.matches(dto.password(), user.getPassword())){
			throw new RestApiException(INVALID_PASSWORD);
		}

		String roles = user.getRoles().stream()
			.map(UserRole::name)
			.collect(Collectors.joining(","));

		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		Authentication authentication =
			new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String accessToken = jwtUtil.createJwt(user.getUsername(), roles, 60 * 60 * 10L);
		String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), roles, 60 * 60 *24* 30L);
		return new TokenResponseDto(accessToken,refreshToken);
	}

	public TokenResponseDto refreshAccessToken(RefreshTokenRequestDto requestDto) {
		if (jwtUtil.isExpired(requestDto.refreshToken())){
			throw new RestApiException(INVALID_REFRESH_TOKEN);
		}

		String username = jwtUtil.getUsername(requestDto.refreshToken());
		User user = userRepository.findByUsername(username).orElseThrow(
			()-> new RestApiException(USER_NOT_FOUND)
		);

		String newAccessToken = jwtUtil.createJwt(user.getUsername(), user.getRoles().stream()
			.map(UserRole::name).collect(Collectors.joining(",")), 60 * 60 * 10L);

		String newRefreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRoles().stream()
			.map(UserRole::name).collect(Collectors.joining(",")), 60 * 60 * 24 * 30L);

		return new TokenResponseDto(newAccessToken, newRefreshToken);
	}
}
