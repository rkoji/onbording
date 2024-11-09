package com.onbording.auth.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.onbording.auth.entity.User;
import com.onbording.auth.entity.dto.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");

		if (authorization == null || !authorization.startsWith("Bearer ")) {

			System.out.println("token null");
			filterChain.doFilter(request, response);

			return;
		}

		String token = authorization.split(" ")[1];

		if (jwtUtil.isExpired(token)) {

			System.out.println("token expired");
			filterChain.doFilter(request, response);

			return;
		}

		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		User user = User.createUser(username, "temppassword","nickname", role);

		CustomUserDetails customUserDetails = new CustomUserDetails(user);

		Authentication authToken =
			new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}
}
