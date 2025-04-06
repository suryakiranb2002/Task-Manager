package com.indpro.task_manager.repository;

import com.indpro.task_manager.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken,Long> {

    Optional<JwtToken> findByJwToken(String token);

}
