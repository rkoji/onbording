package com.onbording.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.onbording.auth.entity.User;
import com.onbording.auth.entity.dto.CustomUserDetails;
import com.onbording.auth.exception.ErrorCode;
import com.onbording.auth.exception.RestApiException;
import com.onbording.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User userData = userRepository.findByUsername(username).orElseThrow(
			()-> new RestApiException(ErrorCode.USER_NOT_FOUND)
		);

		if (userData != null){
			return new CustomUserDetails(userData);
		}

		return null;
	}
}
