package com.onbording.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onbording.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Boolean existsByUsername(String username);
}
