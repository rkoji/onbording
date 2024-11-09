package com.onbording.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.onbording.auth.entity.User;
import com.onbording.auth.entity.dto.request.SignUpRequestDto;
import com.onbording.auth.entity.dto.response.SignUpResponseDto;
import com.onbording.auth.exception.RestApiException;
import com.onbording.auth.jwt.JWTUtil;
import com.onbording.auth.repository.UserRepository;

class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	private JWTUtil jwtUtil;

	private AuthService authService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		authService = new AuthService(userRepository, passwordEncoder,jwtUtil);
	}

	@Test
	void 회원가입_성공(){
		// given
		String username = "newUsername";
		String password = "newPassword";
		String nickname = "newNickname";
		SignUpRequestDto signUpRequestDto = new SignUpRequestDto(username, password, nickname);
		String encodedPassword = "encodedPassword";

		when(userRepository.existsByUsername(username)).thenReturn(false);
		when(passwordEncoder.encode(encodedPassword)).thenReturn(encodedPassword);

		// when
		SignUpResponseDto response = authService.signUp(signUpRequestDto);

		// then
		assertNotNull(response);
		verify(userRepository,times(1)).save(any(User.class));
	}

	@Test
	void 회원가입_실패_username_중복(){
		// given
		SignUpRequestDto dto =
			new SignUpRequestDto("existingUsername", "existingpassword", "existingNickname");
		when(userRepository.existsByUsername("existingUsername")).thenReturn(true);

		// when
		RestApiException exception = assertThrows(RestApiException.class, () -> authService.signUp(dto));

		// then
		assertEquals("존재하는 아이디 입니다.", exception.getMessage());
	}

	@Test
	void 회원가입_실패_password_null(){
		// given
		SignUpRequestDto dto = new SignUpRequestDto("newUsername", null, "newNickname");

		// when
		RestApiException exception = assertThrows(RestApiException.class, () -> authService.signUp(dto));

		// then
		assertEquals("비밀번호는 필수 입력 항목입니다.",exception.getMessage());
	}

	@Test
	void 회원가입_실패_password_empty(){
		// given
		SignUpRequestDto dto = new SignUpRequestDto("newUsername", "", "newNickname");

		// when
		RestApiException exception = assertThrows(RestApiException.class, () -> authService.signUp(dto));

		// then
		assertEquals("비밀번호는 필수 입력 항목입니다.",exception.getMessage());
	}

}