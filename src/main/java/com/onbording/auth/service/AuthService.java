package com.onbording.auth.service;

import static com.onbording.auth.exception.ErrorCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onbording.auth.entity.User;
import com.onbording.auth.entity.dto.request.SignUpRequestDto;
import com.onbording.auth.entity.dto.response.SignUpResponseDto;
import com.onbording.auth.exception.ErrorCode;
import com.onbording.auth.exception.RestApiException;
import com.onbording.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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
}
