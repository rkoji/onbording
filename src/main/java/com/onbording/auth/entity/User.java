package com.onbording.auth.entity;

import java.util.ArrayList;
import java.util.List;

import com.onbording.auth.entity.dto.request.SignUpRequestDto;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;
	private String nickname;

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private List<UserRole> roles = new ArrayList<>();

	public static User signup(SignUpRequestDto dto,String password) {
		return User.builder()
			.username(dto.username())
			.password(password)
			.nickname(dto.nickname())
			.roles(List.of(UserRole.ROLE_USER))
			.build();
	}

	public static User createUser(String username,String password,String nickname,String role) {
		return User.builder()
			.username(username)
			.password(password)
			.nickname(nickname)
			.roles(List.of(UserRole.valueOf(role)))
			.build();
	}
}
