package com.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.authentication.entity.User;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
