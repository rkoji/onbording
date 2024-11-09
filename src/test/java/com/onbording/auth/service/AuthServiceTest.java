package com.onbording.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.onbording.auth.entity.User;
import com.onbording.auth.entity.dto.request.SignRequestDto;
import com.onbording.auth.entity.dto.request.SignUpRequestDto;
import com.onbording.auth.entity.dto.response.SignUpResponseDto;
import com.onbording.auth.entity.dto.response.TokenResponseDto;
import com.onbording.auth.exception.RestApiException;
import com.onbording.auth.jwt.JWTUtil;
import com.onbording.auth.repository.UserRepository;

class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
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

	@Test
	void 로그인_성공(){
		// given
		String username = "testUser";
		String password = "testPassword";
		String role = "ROLE_USER";
		SignRequestDto signRequestDto = new SignRequestDto(username, password);
		User user = User.createUser(username, password, "testNickname", role);
		String encodedPassword = "encodedPassword";

		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
		when(jwtUtil.createJwt(username, role, 60 * 60 * 10L)).thenReturn("accessToken");
		when(jwtUtil.createRefreshToken(username, role, 60 * 60 * 24 * 30L)).thenReturn("refreshToken");

		// when
		TokenResponseDto response = authService.login(signRequestDto);

		// then
		assertNotNull(response);
		assertEquals("accessToken",response.accessToken());
		assertEquals("refreshToken",response.refreshToken());

		verify(userRepository, times(1)).findByUsername(username);
		verify(passwordEncoder, times(1)).matches(password, user.getPassword());
		verify(jwtUtil, times(1)).createJwt(username, role, 60 * 60 * 10L);
		verify(jwtUtil, times(1)).createRefreshToken(username, role, 60 * 60 * 24 * 30L);
	}

	@Test
	void 로그인_실패_사용자없음() {
		// given
		SignRequestDto signRequestDto = new SignRequestDto("nonExistingUser", "password");

		when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

		// when
		RestApiException exception = assertThrows(RestApiException.class, () -> authService.login(signRequestDto));

		// then
		assertEquals("존재하지 않는 아이디입니다.", exception.getMessage());
		verify(userRepository, times(1)).findByUsername("nonExistingUser");
	}

	@Test
	void 로그인_실패_비밀번호불일치() {
		// given
		String username = "testUser";
		String password = "wrongPassword";
		SignRequestDto signRequestDto = new SignRequestDto(username, password);
		User user = User.createUser(username, "password", "correctPassword", "ROLE_USER");

		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

		// when
		RestApiException exception = assertThrows(RestApiException.class, () -> authService.login(signRequestDto));

		// then
		assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
		verify(userRepository, times(1)).findByUsername(username);
		verify(passwordEncoder, times(1)).matches(password, user.getPassword());
	}


}